<?php
require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../config/response.php';
require_once __DIR__ . '/../config/auth.php';

$user = getAuthUser();
requireManager($user);

$name = input('name');
$email = strtolower(input('email'));
$phone = input('phone');
$password = input('password');
$isActive = (int)input('is_active', 1) === 1 ? 1 : 0;

if ($name === '' || $email === '' || $password === '') {
    jsonResponse(false, 'Nama, email, password wajib', null, 422);
}
if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    jsonResponse(false, 'Format email tidak valid', null, 422);
}

$pdo = getPDO();
$roleId = (int)$pdo->query("SELECT id FROM roles WHERE name='petugas' LIMIT 1")->fetchColumn();
if ($roleId <= 0) {
    jsonResponse(false, 'Role petugas belum tersedia', null, 500);
}

$stmt = $pdo->prepare('SELECT id FROM users WHERE email=? LIMIT 1');
$stmt->execute([$email]);
if ($stmt->fetch()) {
    jsonResponse(false, 'Email sudah dipakai', null, 409);
}

$hash = password_hash($password, PASSWORD_DEFAULT);
$stmt = $pdo->prepare('INSERT INTO users (role_id, name, email, phone, password_hash, is_active, created_at, updated_at) VALUES (?,?,?,?,?,?,NOW(),NOW())');
$stmt->execute([$roleId, $name, $email, $phone, $hash, $isActive]);
jsonResponse(true, 'Petugas berhasil ditambahkan', ['id' => (int)$pdo->lastInsertId()]);
?>
