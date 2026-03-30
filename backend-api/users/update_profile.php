<?php
require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../config/response.php';
require_once __DIR__ . '/../config/auth.php';

$authUser = getAuthUser();
$targetId = (int)input('user_id', (int)$authUser['id']);
if ($authUser['role_name'] !== 'manager') {
    $targetId = (int)$authUser['id'];
}

$name = input('name');
$phone = input('phone');
$password = input('password');
$isActiveRaw = input('is_active', null);

if ($name === '') {
    jsonResponse(false, 'Nama wajib diisi', null, 422);
}

$pdo = getPDO();
$stmt = $pdo->prepare('SELECT id FROM users WHERE id=? LIMIT 1');
$stmt->execute([$targetId]);
if (!$stmt->fetch()) {
    jsonResponse(false, 'User tidak ditemukan', null, 404);
}

if ($password !== '') {
    $hash = password_hash($password, PASSWORD_DEFAULT);
    if ($authUser['role_name'] === 'manager' && $isActiveRaw !== null) {
        $isActive = (int)$isActiveRaw === 1 ? 1 : 0;
        $stmt = $pdo->prepare('UPDATE users SET name=?, phone=?, password_hash=?, is_active=?, updated_at=NOW() WHERE id=?');
        $stmt->execute([$name, $phone, $hash, $isActive, $targetId]);
    } else {
        $stmt = $pdo->prepare('UPDATE users SET name=?, phone=?, password_hash=?, updated_at=NOW() WHERE id=?');
        $stmt->execute([$name, $phone, $hash, $targetId]);
    }
} else {
    if ($authUser['role_name'] === 'manager' && $isActiveRaw !== null) {
        $isActive = (int)$isActiveRaw === 1 ? 1 : 0;
        $stmt = $pdo->prepare('UPDATE users SET name=?, phone=?, is_active=?, updated_at=NOW() WHERE id=?');
        $stmt->execute([$name, $phone, $isActive, $targetId]);
    } else {
        $stmt = $pdo->prepare('UPDATE users SET name=?, phone=?, updated_at=NOW() WHERE id=?');
        $stmt->execute([$name, $phone, $targetId]);
    }
}

jsonResponse(true, 'Profil berhasil diupdate');
?>
