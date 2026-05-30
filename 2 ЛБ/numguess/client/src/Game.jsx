import { useState } from 'react';

const API = 'http://localhost:3001/api';

export default function Game({ username }) {
  const [guess,    setGuess]    = useState('');
  const [hint,     setHint]     = useState('');
  const [attempts, setAttempts] = useState(0);
  const [message,  setMessage]  = useState('');
  const [won,      setWon]      = useState(false);
  const [history,  setHistory]  = useState([]);

  const sendGuess = async () => {
    const num = parseInt(guess);
    if (!guess || isNaN(num) || num < 1 || num > 100) {
      setHint('Введіть число від 1 до 100!');
      return;
    }

    const res  = await fetch(`${API}/guess`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, guess: num }),
    });
    const data = await res.json();

    setHistory(prev => [{ guess: num, hint: data.hint || '✅' }, ...prev]);
    setAttempts(data.attempts || attempts + 1);
    setGuess('');

    if (data.result === 'win') {
      setMessage(data.message);
      setHint('');
      setWon(true);
    } else {
      setHint(data.hint);
      setMessage('');
      setWon(false);
    }
  };

  const newGame = async () => {
    await fetch(`${API}/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username }),
    });
    setGuess(''); setHint(''); setAttempts(0);
    setMessage(''); setWon(false); setHistory([]);
  };

  return (
    <div>
      <div style={{ background: '#fff', borderRadius: 10,
                    padding: 24, marginBottom: 20,
                    boxShadow: '0 2px 8px #0001' }}>
        <p style={{ color: '#888', marginBottom: 16 }}>
          Я загадав число від <strong>1</strong> до <strong>100</strong>.
          Спробуй вгадати!
        </p>

        {!won ? (
          <div style={{ display: 'flex', gap: 10, marginBottom: 16 }}>
            <input
              type="number" min="1" max="100"
              placeholder="Введіть число..."
              value={guess}
              onChange={e => setGuess(e.target.value)}
              onKeyDown={e => e.key === 'Enter' && sendGuess()}
              style={{ flex: 1, padding: 10, borderRadius: 6,
                       border: '1px solid #ddd', fontSize: 16 }}
            />
            <button onClick={sendGuess} style={{
              padding: '10px 24px', background: '#4a6cf7',
              color: '#fff', border: 'none', borderRadius: 6,
              cursor: 'pointer', fontWeight: 'bold'
            }}>Вгадати</button>
          </div>
        ) : null}

        {hint && (
          <p style={{ fontSize: 20, fontWeight: 'bold',
                      color: hint === 'Більше!' ? '#e53935' : '#1e88e5' }}>
            {hint}
          </p>
        )}
        {message && (
          <p style={{ fontSize: 20, fontWeight: 'bold', color: '#43a047' }}>
            🎉 {message}
          </p>
        )}
        <p style={{ color: '#666', marginTop: 8 }}>
          Спроб: <strong>{won ? 0 : attempts}</strong>
        </p>

        <button onClick={newGame} style={{
          marginTop: 8, padding: '8px 20px',
          background: '#4CAF50', color: '#fff',
          border: 'none', borderRadius: 6, cursor: 'pointer'
        }}>🔄 Нова гра</button>
      </div>

      {history.length > 0 && (
        <div style={{ background: '#fff', borderRadius: 10,
                      padding: 20, boxShadow: '0 2px 8px #0001' }}>
          <h3 style={{ marginTop: 0 }}>Історія спроб</h3>
          {history.map((h, i) => (
            <div key={i} style={{
              display: 'flex', justifyContent: 'space-between',
              padding: '6px 0', borderBottom: '1px solid #f0f0f0'
            }}>
              <span>Спроба: <strong>{h.guess}</strong></span>
              <span style={{
                color: h.hint === 'Більше!' ? '#e53935' :
                       h.hint === 'Менше!'  ? '#1e88e5' : '#43a047',
                fontWeight: 'bold'
              }}>{h.hint}</span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
