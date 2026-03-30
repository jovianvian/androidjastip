<?php
require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../config/response.php';
require_once __DIR__ . '/../config/auth.php';

$authUser = getAuthUser();
$customerId = (int)input('customer_id', 0);
$productId = (int)input('product_id', 0);
$quantity = (int)input('quantity', 0);
$notes = input('notes');

if ($customerId <= 0 || $productId <= 0 || $quantity <= 0) {
    jsonResponse(false, 'customer_id, product_id, quantity wajib valid', null, 422);
}

$pdo = getPDO();
$pdo->beginTransaction();
try {
    $stmt = $pdo->prepare('SELECT id FROM customers WHERE id=? AND is_active=1 LIMIT 1');
    $stmt->execute([$customerId]);
    if (!$stmt->fetch()) {
        throw new Exception('Customer tidak ditemukan');
    }

    $stmt = $pdo->prepare('SELECT id, price, is_active FROM products WHERE id=? LIMIT 1');
    $stmt->execute([$productId]);
    $product = $stmt->fetch();
    if (!$product || (int)$product['is_active'] !== 1) {
        throw new Exception('Produk tidak ditemukan/aktif');
    }

    $unitPrice = (int)$product['price'];
    $total = $unitPrice * $quantity;
    $orderCode = 'ORD-' . date('Ymd-His') . '-' . random_int(100, 999);

    $stmt = $pdo->prepare('INSERT INTO orders (order_code, customer_id, product_id, quantity, unit_price, total_amount, status, notes, created_by, created_at, updated_at)
                           VALUES (?,?,?,?,?,?,"pending",?,?,NOW(),NOW())');
    $stmt->execute([$orderCode, $customerId, $productId, $quantity, $unitPrice, $total, $notes, (int)$authUser['id']]);
    $orderId = (int)$pdo->lastInsertId();

    $stmt = $pdo->prepare('INSERT INTO order_status_logs (order_id, old_status, new_status, changed_by, changed_at, notes) VALUES (?, NULL, "pending", ?, NOW(), ?)');
    $stmt->execute([$orderId, (int)$authUser['id'], 'Order dibuat']);

    $pdo->commit();
    jsonResponse(true, 'Order berhasil dibuat', ['id' => $orderId, 'order_code' => $orderCode, 'total_amount' => $total]);
} catch (Exception $e) {
    $pdo->rollBack();
    jsonResponse(false, $e->getMessage(), null, 400);
}
?>
