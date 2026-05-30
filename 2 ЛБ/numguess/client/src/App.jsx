import { useState } from 'react';
import Game        from './Game';
import Leaderboard from './Leaderboard';

const API = 'http://localhost:3001/api';

export default function App() {
  const [page, setPage]     = useState('login');
  const [username, setUsername] = useState('');
  const [input, setInput]   = useState('');
  const [error, setError]   = useState('');

  const login = async () => {
    const name = input.trim();
    if (!name) { setError("Введіть нікнейм!"); return; }
    await fetch(`${API}/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username: name }),
    });
    setUsername(name);
    setPage('game');
  };

  const navBtn = (target, label) => (
    <button onClick={() => setPage(target)} style={{
      marginRight: 12, padding: '8px 20px',
      background: page === target ? '#4a6cf7' : '#eee',
      color: page === target ? '#fff' : '#333',
      border: 'none', borderRadius: 6, cursor: 'pointer', fontWeight: 'bold'
    }}>{label}</button>
  );

  if (page === 'login') return (
    <div style={{ fontFamily: 'Arial, sans-serif', maxWidth: 420,
                  margin: '80px auto', padding: 32,
                  background: '#fff', borderRadius: 12,
                  boxShadow: '0 4px 20px #0001' }}>
      <h1 style={{ marginBottom: 8 }}>🎯 Вгадай число</h1>
      <p style={{ color: '#888', marginBottom: 24 }}>
        Вгадайте число від 1 до 100
      </p>
      <input
        placeholder="Ваш нікнейм"
        value={input}
        onChange={e => { setInput(e.target.value); setError(''); }}
        onKeyDown={e => e.key === 'Enter' && login()}
        style={{ width: '100%', padding: 10, marginBottom: 12,
                 borderRadius: 6, border: '1px solid #ddd',
                 boxSizing: 'border-box', fontSize: 16 }}
      />
      {error && <p style={{ color: 'red', marginBottom: 8 }}>{error}</p>}
      <button onClick={login} style={{
        width: '100%', padding: 12, background: '#4a6cf7',
        color: '#fff', border: 'none', borderRadius: 6,
        cursor: 'pointer', fontSize: 16, fontWeight: 'bold'
      }}>Грати</button>
    </div>
  );

  return (
    <div style={{ fontFamily: 'Arial, sans-serif',
                  maxWidth: 700, margin: '0 auto', padding: 24 }}>
      <h1 style={{ marginBottom: 4 }}>🎯 Вгадай число</h1>
      <p style={{ color: '#888', marginBottom: 16 }}>
        Гравець: <strong>{username}</strong>
      </p>
      <nav style={{ marginBottom: 24 }}>
        {navBtn('game', 'Гра')}
        {navBtn('leaderboard', '🏆 Лідерборд')}
      </nav>
      {page === 'game'        && <Game username={username} />}
      {page === 'leaderboard' && <Leaderboard />}
    </div>
  );
}
