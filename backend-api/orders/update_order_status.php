<?php
require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../config/response.php';
require_once __DIR__ . '/../config/auth.php';

$authUser = getAuthUser();
$orderId = (int)input('order_id', 0);
$newStatus = strtolower(input('status'));
$note = input('note');

$validStatuses = ['pending', 'diproses', 'dibelikan', 'dikirim', 'selesai', 'dibatalkan'];
if ($orderId <= 0 || !in_array($newStatus, $validStatuses, true)) {
    jsonResponse(false, 'order_id / status tidak valid', null, 422);
}

$pdo = getPDO();
$pdo->beginTransaction();
try {
    $stmt = $pdo->prepare('SELECT id, status, created_by FROM orders WHERE id=? LIMIT 1');
    $stmt->execute([$orderId]);
    $order = $stmt->fetch();
    if (!$order) {
        throw new Exception('Order tidak ditemukan');
    }

    if ($authUser['role_name'] !== 'manager' && (int)$order['created_by'] !== (int)$authUser['id']) {
        throw new Exception('Forbidden: bukan pemilik order');
    }

    $oldStatus = $order['status'];
    if ($oldStatus === $newStatus) {
        throw new Exception('Status sama, tidak ada perubahan');
    }

    $stmt = $pdo->prepare('UPDATE orders SET status=?, updated_at=NOW() WHERE id=?');
    $stmt->execute([$newStatus, $orderId]);

    $stmt = $pdo->prepare('INSERT INTO order_status_logs (order_id, old_status, new_status, changed_by, changed_at, notes) VALUES (?, ?, ?, ?, NOW(), ?)');
    $stmt->execute([$orderId, $oldStatus, $newStatus, (int)$authUser['id'], $note]);

    $pdo->commit();
    jsonResponse(true, 'Status order berhasil diupdate');
} catch (Exception $e) {
    $pdo->rollBack();
    jsonResponse(false, $e->getMessage(), null, 400);
}
?>
