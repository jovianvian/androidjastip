<?php
require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../config/response.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    jsonResponse(false, 'Method tidak diizinkan', null, 405);
}

$email = strtolower(input('email'));
$password = input('password');

if ($email === '' || $password === '') {
    jsonResponse(false, 'Email dan password wajib diisi', null, 422);
}

$pdo = getPDO();
$stmt = $pdo->prepare('SELECT u.id, u.name, u.email, u.password_hash, u.is_active, r.name AS role_name FROM users u JOIN roles r ON r.id = u.role_id WHERE u.email=? LIMIT 1');
$stmt->execute([$email]);
$user = $stmt->fetch();

if (!$user || !password_verify($password, $user['password_hash'])) {
    jsonResponse(false, 'Email atau password salah', null, 401);
}

if ((int)$user['is_active'] !== 1) {
    jsonResponse(false, 'Akun tidak aktif', null, 403);
}

jsonResponse(true, 'Login berhasil', [
    'id' => (int)$user['id'],
    'name' => $user['name'],
    'email' => $user['email'],
    'role' => $user['role_name']
]);
?>
