import { useState, useEffect } from 'react';

const API = 'http://localhost:3001/api';

export default function Leaderboard() {
  const [board, setBoard] = useState([]);

  const load = () =>
    fetch(`${API}/leaderboard`).then(r => r.json()).then(setBoard);

  useEffect(() => { load(); }, []);

  const medals = ['🥇', '🥈', '🥉'];

  return (
    <div style={{ background: '#fff', borderRadius: 10,
                  padding: 24, boxShadow: '0 2px 8px #0001' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between',
                    alignItems: 'center', marginBottom: 16 }}>
        <h2 style={{ margin: 0 }}>🏆 Лідерборд</h2>
        <button onClick={load} style={{
          padding: '6px 16px', background: '#4a6cf7',
          color: '#fff', border: 'none', borderRadius: 6, cursor: 'pointer'
        }}>🔄 Оновити</button>
      </div>

      {board.length === 0 ? (
        <p style={{ color: '#888' }}>Ще немає результатів. Зіграй першим!</p>
      ) : (
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr style={{ background: '#f0f4ff' }}>
              {['#', 'Гравець', '🏆 Перемог', '⚡ Кращий результат'].map(h => (
                <th key={h} style={{ padding: '10px 16px',
                  textAlign: 'left', borderBottom: '2px solid #e0e0e0' }}>
                  {h}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {board.map((p, i) => (
              <tr key={p.username} style={{
                background: i % 2 === 0 ? '#fff' : '#f9f8ff'
              }}>
                <td style={{ padding: '10px 16px' }}>
                  {i < 3 ? medals[i] : i + 1}
                </td>
                <td style={{ padding: '10px 16px', fontWeight: 'bold' }}>
                  {p.username}
                </td>
                <td style={{ padding: '10px 16px', color: '#43a047' }}>
                  {p.wins}
                </td>
                <td style={{ padding: '10px 16px', color: '#1e88e5' }}>
                  {p.bestScore} спроб
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
