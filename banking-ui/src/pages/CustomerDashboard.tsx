import { useState, useEffect } from 'react';
import Navbar from '../components/Navbar';
import { User, Transaction, CardBalance, TransactionRequest } from '../types/types';
import { getBalance, getTransactions, processTransaction } from '../services/api';

interface CustomerDashboardProps {
  user: User;
  onLogout: () => void;
}

const CustomerDashboard = ({ user, onLogout }: CustomerDashboardProps) => {
  const [balance, setBalance] = useState<CardBalance | null>(null);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

  // Top-up form
  const [amount, setAmount] = useState('');
  const [pin, setPin] = useState('');

  useEffect(() => {
    fetchData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [user.cardNumber]);

  const fetchData = async () => {
    if (!user.cardNumber) return;

    setLoading(true);
    try {
      const balanceResponse = await getBalance(user.cardNumber);
      if (balanceResponse?.success) {
        setBalance(balanceResponse.data);
      }

      const transactionsResponse = await getTransactions(user.cardNumber);
      if (transactionsResponse?.success) {
        setTransactions(transactionsResponse.data);
      }
    } catch (error) {
      console.error('Error fetching data:', error);
      setMessage({ type: 'error', text: 'Failed to load data.' });
    } finally {
      setLoading(false);
    }
  };

  const handleTopup = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage(null);

    if (!user.cardNumber) {
      setMessage({ type: 'error', text: 'No card number found.' });
      return;
    }

    const parsedAmount = Number.parseFloat(amount);
    if (Number.isNaN(parsedAmount) || parsedAmount <= 0) {
      setMessage({ type: 'error', text: 'Enter a valid amount.' });
      return;
    }

    const request: TransactionRequest = {
      cardNumber: user.cardNumber,
      pin,
      amount: parsedAmount,
      type: 'topup'
    };

    setLoading(true);
    try {
      const response = await processTransaction(request);

      if (response?.success) {
        setMessage({ type: 'success', text: response.message || 'Top-up successful.' });
        setAmount('');
        setPin('');
        await fetchData();
      } else {
        setMessage({ type: 'error', text: response?.message || 'Top-up failed.' });
      }
    } catch (error) {
      console.error('Top-up error:', error);
      setMessage({ type: 'error', text: 'An error occurred during top-up.' });
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString?: string) => {
    if (!dateString) return '-';
    return new Date(dateString).toLocaleString();
  };

  const displayName = balance?.customerName ?? (user as any).username ?? user.cardNumber ?? '';

  return (
    <div style={{ minHeight: '100vh', background: '#f3f4f6' }}>
      <Navbar user={user} onLogout={onLogout} />

      <div style={{ maxWidth: '1200px', margin: '0 auto', padding: '24px' }}>
        {message && (
          <div className={`alert alert-${message.type}`}>
            {message.text}
          </div>
        )}

        {/* Balance Card */}
        <div className="card" style={{
          background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
          color: 'white',
          padding: '20px',
          marginBottom: '20px',
          borderRadius: '8px'
        }}>
          <h2 style={{ fontSize: '18px', marginBottom: '16px', opacity: 0.9 }}>
            Current Balance
          </h2>
          <div style={{ fontSize: '48px', fontWeight: 'bold', marginBottom: '8px' }}>
            ${balance?.balance?.toFixed(2) ?? '0.00'}
          </div>
          <p style={{ opacity: 0.9 }}>
            Card: ****{user.cardNumber?.slice(-4)}
          </p>
          <p style={{ opacity: 0.9 }}>
            {displayName}
          </p>
        </div>

        {/* Top-up Form */}
        <div className="card" style={{ padding: '20px', borderRadius: '8px', marginBottom: '20px' }}>
          <h2 style={{ fontSize: '20px', fontWeight: 'bold', marginBottom: '20px', color: '#1f2937' }}>
            Top-Up Account
          </h2>
          <form onSubmit={handleTopup} style={{ display: 'flex', gap: '12px', flexWrap: 'wrap' }}>
            <input
              type="number"
              step="0.01"
              min="0.01"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              placeholder="Amount"
              style={{
                flex: '1',
                minWidth: '150px',
                padding: '12px',
                border: '2px solid #e5e7eb',
                borderRadius: '8px',
                fontSize: '16px'
              }}
              required
            />
            <input
              type="password"
              value={pin}
              onChange={(e) => setPin(e.target.value)}
              placeholder="PIN"
              maxLength={4}
              style={{
                flex: '1',
                minWidth: '150px',
                padding: '12px',
                border: '2px solid #e5e7eb',
                borderRadius: '8px',
                fontSize: '16px'
              }}
              required
            />
            <button
              type="submit"
              className="btn-primary"
              disabled={loading}
              style={{ minWidth: '150px' }}
            >
              {loading ? 'Processing...' : 'Top-Up'}
            </button>
            <button
              type="button"
              className="btn-secondary"
              onClick={fetchData}
              disabled={loading}
            >
              Refresh
            </button>
          </form>
        </div>

        {/* Transaction History */}
        <div className="card" style={{ padding: '20px', borderRadius: '8px' }}>
          <h2 style={{ fontSize: '20px', fontWeight: 'bold', marginBottom: '20px', color: '#1f2937' }}>
            Transaction History
          </h2>
          <div style={{ overflowX: 'auto' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Type</th>
                  <th>Amount</th>
                  <th>Date & Time</th>
                  <th>Status</th>
                  <th>Details</th>
                </tr>
              </thead>
              <tbody>
                {transactions.length === 0 ? (
                  <tr>
                    <td colSpan={6} style={{ textAlign: 'center', padding: '40px', color: '#6b7280' }}>
                      No transactions yet
                    </td>
                  </tr>
                ) : (
                  transactions.map((tx) => (
                    <tr key={tx.id}>
                      <td>{tx.id}</td>
                      <td style={{ textTransform: 'capitalize' }}>{tx.type}</td>
                      <td style={{ fontWeight: 600 }}>
                        ${tx.amount.toFixed(2)}
                      </td>
                      <td>{formatDate(tx.timestamp)}</td>
                      <td className={`status-${(tx.status || '').toString().toLowerCase()}`}>
                        {tx.status}
                      </td>
                      <td style={{ color: '#6b7280', fontSize: '14px' }}>
                        {tx.reason ?? '-'}
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CustomerDashboard;