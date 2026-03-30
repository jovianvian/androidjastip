<?php
require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../config/response.php';
require_once __DIR__ . '/../config/auth.php';

$authUser = getAuthUser();
$targetId = (int)input('user_id', (int)$authUser['id']);

if ($authUser['role_name'] !== 'manager') {
    $targetId = (int)$authUser['id'];
}

$pdo = getPDO();
$stmt = $pdo->prepare('SELECT u.id, u.name, u.email, u.phone, u.is_active, r.name AS role_name, u.created_at FROM users u JOIN roles r ON r.id=u.role_id WHERE u.id=? LIMIT 1');
$stmt->execute([$targetId]);
$data = $stmt->fetch();
if (!$data) {
    jsonResponse(false, 'User tidak ditemukan', null, 404);
}
jsonResponse(true, 'Profil user', $data);
?>
