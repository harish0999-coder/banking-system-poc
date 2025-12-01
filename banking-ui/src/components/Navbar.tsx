import { User } from '../types/types';

interface NavbarProps {
  user: User;
  onLogout: () => void;
}

const Navbar = ({ user, onLogout }: NavbarProps) => {
  return (
    <nav style={{
      background: 'white',
      boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
      padding: '16px 24px',
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center'
    }}>
      <div>
        <h2 style={{ fontSize: '24px', fontWeight: 'bold', color: '#1f2937' }}>
          Banking System POC
        </h2>
        <p style={{ fontSize: '14px', color: '#6b7280' }}>
          {user.role === 'ADMIN' ? 'Super Admin Dashboard' : 'Customer Dashboard'}
        </p>
      </div>
      <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
        <span style={{ color: '#374151', fontWeight: '600' }}>
          {user.username}
        </span>
        <button onClick={onLogout} className="btn-danger">
          Logout
        </button>
      </div>
    </nav>
  );
};

export default Navbar;