<?php
require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../config/response.php';
require_once __DIR__ . '/../config/auth.php';

$authUser = getAuthUser();
$pdo = getPDO();

$status = input('status');
$isManager = ($authUser['role_name'] === 'manager');

$sql = 'SELECT o.id, o.order_code, o.quantity, o.unit_price, o.total_amount, o.status, o.notes, o.created_at,
               c.id AS customer_id, c.name AS customer_name, c.phone AS customer_phone,
               p.id AS product_id, p.name AS product_name,
               u.id AS staff_id, u.name AS staff_name
        FROM orders o
        JOIN customers c ON c.id=o.customer_id
        JOIN products p ON p.id=o.product_id
        JOIN users u ON u.id=o.created_by
        WHERE 1=1';
$params = [];

if (!$isManager) {
    $sql .= ' AND o.created_by=?';
    $params[] = (int)$authUser['id'];
}
if ($status !== '') {
    $sql .= ' AND o.status=?';
    $params[] = $status;
}
$sql .= ' ORDER BY o.id DESC';

$stmt = $pdo->prepare($sql);
$stmt->execute($params);
jsonResponse(true, 'Daftar order', $stmt->fetchAll());
?>
