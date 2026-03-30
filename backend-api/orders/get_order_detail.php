<?php
require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../config/response.php';
require_once __DIR__ . '/../config/auth.php';

$authUser = getAuthUser();
$orderId = (int)input('order_id', 0);
if ($orderId <= 0) {
    jsonResponse(false, 'order_id tidak valid', null, 422);
}

$pdo = getPDO();
$sql = 'SELECT o.id, o.order_code, o.quantity, o.unit_price, o.total_amount, o.status, o.notes, o.created_at,
               c.id AS customer_id, c.name AS customer_name, c.phone AS customer_phone, c.address AS customer_address,
               p.id AS product_id, p.name AS product_name, p.origin_city, p.eta_days,
               u.id AS staff_id, u.name AS staff_name
        FROM orders o
        JOIN customers c ON c.id=o.customer_id
        JOIN products p ON p.id=o.product_id
        JOIN users u ON u.id=o.created_by
        WHERE o.id=? LIMIT 1';
$stmt = $pdo->prepare($sql);
$stmt->execute([$orderId]);
$order = $stmt->fetch();
if (!$order) {
    jsonResponse(false, 'Order tidak ditemukan', null, 404);
}

if ($authUser['role_name'] !== 'manager' && (int)$order['staff_id'] !== (int)$authUser['id']) {
    jsonResponse(false, 'Forbidden', null, 403);
}

$logStmt = $pdo->prepare('SELECT old_status, new_status, notes, changed_at FROM order_status_logs WHERE order_id=? ORDER BY id DESC');
$logStmt->execute([$orderId]);
$order['status_logs'] = $logStmt->fetchAll();

jsonResponse(true, 'Detail order', $order);
?>
