function checkJSON(text) {
    try {
        const parsed = JSON.parse(text);
        const type = Array.isArray(parsed) ? 'масив' :
                     typeof parsed === 'object' ? 'об\'єкт' :
                     typeof parsed;
        return { valid: true, type };
    } catch (e) {
        return { valid: false, error: e.message };
    }
}

const testCases = [
    '{"user": "Цисельський", "role": "student"}',
    '[10, 20, 30, 40, 50]',
    'true',
    '{name: "без лапок"}',
    '{"unclosed": [1, 2, 3',
    '',
];

console.log('=== Перевірка JSON-рядків ===\n');
testCases.forEach((text, i) => {
    const result = checkJSON(text);
    const preview = text.length > 35 ? text.slice(0, 35) + '...' : text || '(порожній рядок)';
    if (result.valid) {
        console.log(`[${i+1}] Дійсний JSON (${result.type}): ${preview}`);
    } else {
        console.log(`[${i+1}] Недійсний JSON: ${preview}`);
        console.log(`     Помилка: ${result.error}`);
    }
});
