const express = require('express');
const cors    = require('cors');
const app     = express();

app.use(cors());
app.use(express.json());

let players = {};

function newSecret() {
  return Math.floor(Math.random() * 100) + 1;
}

app.post('/api/register', (req, res) => {
  const { username } = req.body;
  if (!players[username]) {
    players[username] = {
      attempts: 0,
      wins: 0,
      bestScore: null,
      currentSecret: newSecret(),
    };
  } else {
    players[username].currentSecret = newSecret();
    players[username].attempts = 0;
  }
  res.json({ username, message: 'Нова гра розпочата!' });
});

app.post('/api/guess', (req, res) => {
  const { username, guess } = req.body;
  const player = players[username];

  if (!player) {
    return res.status(404).json({ error: 'Гравця не знайдено' });
  }

  const number = parseInt(guess);
  player.attempts++;

  if (number === player.currentSecret) {
    player.wins++;
    if (player.bestScore === null || player.attempts < player.bestScore) {
      player.bestScore = player.attempts;
    }
    const attempts = player.attempts;
    player.currentSecret = newSecret();
    player.attempts = 0;
    return res.json({
      result: 'win',
      message: `Вгадав за ${attempts} спроб!`,
      attempts,
    });
  }

  const hint = number < player.currentSecret ? 'Більше!' : 'Менше!';

  res.json({
    result: 'miss',
    hint,
    attempts: player.attempts,
  });
});

app.get('/api/leaderboard', (req, res) => {
  const board = Object.entries(players)
    .filter(([, p]) => p.wins > 0)
    .map(([username, p]) => ({
      username,
      wins: p.wins,
      bestScore: p.bestScore,
    }))
    .sort((a, b) => b.wins - a.wins || a.bestScore - b.bestScore);
  res.json(board);
});

app.listen(3001, () => console.log('Server on port 3001'));
