<?php
require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../config/response.php';
require_once __DIR__ . '/../config/auth.php';

$user = getAuthUser();
requireManager($user);
$pdo = getPDO();
$stmt = $pdo->prepare('SELECT u.id, u.name, u.email, u.phone, u.is_active, u.created_at FROM users u JOIN roles r ON r.id=u.role_id WHERE r.name="petugas" ORDER BY u.id DESC');
$stmt->execute();
jsonResponse(true, 'Daftar petugas', $stmt->fetchAll());
?>
