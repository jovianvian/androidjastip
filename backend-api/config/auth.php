<?php
require_once __DIR__ . '/database.php';
require_once __DIR__ . '/response.php';

function getAuthUser(): array {
    $userId = isset($_SERVER['HTTP_X_USER_ID']) ? (int)$_SERVER['HTTP_X_USER_ID'] : (int)input('user_id', 0);
    if ($userId <= 0) {
        jsonResponse(false, 'Unauthorized: user_id tidak valid', null, 401);
    }

    $pdo = getPDO();
    $stmt = $pdo->prepare('SELECT u.id, u.name, u.email, u.is_active, r.name AS role_name FROM users u JOIN roles r ON r.id=u.role_id WHERE u.id=? LIMIT 1');
    $stmt->execute([$userId]);
    $user = $stmt->fetch();
    if (!$user || (int)$user['is_active'] !== 1) {
        jsonResponse(false, 'Unauthorized: user tidak aktif', null, 401);
    }
    return $user;
}

function requireManager(array $user): void {
    if (($user['role_name'] ?? '') !== 'manager') {
        jsonResponse(false, 'Forbidden: hanya manager', null, 403);
    }
}
?>
