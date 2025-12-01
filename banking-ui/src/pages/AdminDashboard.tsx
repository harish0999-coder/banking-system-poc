import { useEffect, useState } from "react";
import type { User, ApiResponse } from "../types/types";
import { getAllTransactions } from "../services/api";

type Props = {
  user: User;
  onLogout: () => void;
};

export default function AdminDashboard({ user, onLogout }: Props) {
  const [txs, setTxs] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    (async () => {
      setLoading(true);
      const res: ApiResponse = await getAllTransactions();  // ✅ FIXED
      if (res.success && Array.isArray(res.data)) setTxs(res.data);
      setLoading(false);
    })();
  }, []);

  return (
    <div style={{ padding: 20 }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <h2>Super Admin Dashboard</h2>
        <div>
          <span style={{ marginRight: 12 }}>{user.username}</span>
          <button onClick={onLogout}>Logout</button>
        </div>
      </div>

      <section style={{ marginTop: 12 }}>
        {loading ? (
          <p>Loading transactions...</p>
        ) : (
          <table style={{ width: "100%", borderCollapse: "collapse" }}>
            <thead>
              <tr>
                <th>ID</th><th>Card</th><th>Type</th><th>Amount</th>
                <th>Status</th><th>Time</th><th>Reason</th>
              </tr>
            </thead>
            <tbody>
              {txs.length === 0 && (
                <tr><td colSpan={7} style={{ textAlign: "center" }}>No transactions</td></tr>
              )}

              {txs.map((tx) => (
                <tr key={tx.id}>
                  <td>{tx.id}</td>
                  <td>{tx.cardNumber}</td>
                  <td>{tx.type}</td>
                  <td>{tx.amount}</td>
                  <td>{tx.status}</td>
                  <td>{tx.timestamp}</td>
                  <td>{tx.reason ?? "-"}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>
    </div>
  );
}
