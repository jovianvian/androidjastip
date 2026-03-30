<?php
require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../config/response.php';
require_once __DIR__ . '/../config/auth.php';

$user = getAuthUser();
$isManager = ($user['role_name'] === 'manager');

$pdo = getPDO();
if ($isManager) {
    $totalCustomers = (int)$pdo->query('SELECT COUNT(*) FROM customers WHERE is_active=1')->fetchColumn();
    $totalProducts = (int)$pdo->query('SELECT COUNT(*) FROM products WHERE is_active=1')->fetchColumn();
    $totalOrders = (int)$pdo->query('SELECT COUNT(*) FROM orders')->fetchColumn();

    $pending = (int)$pdo->query("SELECT COUNT(*) FROM orders WHERE status='pending'")->fetchColumn();
    $selesai = (int)$pdo->query("SELECT COUNT(*) FROM orders WHERE status='selesai'")->fetchColumn();
} else {
    $uid = (int)$user['id'];
    $totalCustomers = (int)$pdo->query('SELECT COUNT(*) FROM customers WHERE is_active=1')->fetchColumn();
    $totalProducts = (int)$pdo->query('SELECT COUNT(*) FROM products WHERE is_active=1')->fetchColumn();

    $stmt = $pdo->prepare('SELECT COUNT(*) FROM orders WHERE created_by=?');
    $stmt->execute([$uid]);
    $totalOrders = (int)$stmt->fetchColumn();

    $stmt = $pdo->prepare("SELECT COUNT(*) FROM orders WHERE status='pending' AND created_by=?");
    $stmt->execute([$uid]);
    $pending = (int)$stmt->fetchColumn();

    $stmt = $pdo->prepare("SELECT COUNT(*) FROM orders WHERE status='selesai' AND created_by=?");
    $stmt->execute([$uid]);
    $selesai = (int)$stmt->fetchColumn();
}

jsonResponse(true, 'Dashboard summary', [
    'total_customers' => $totalCustomers,
    'total_products' => $totalProducts,
    'total_orders' => $totalOrders,
    'total_pending' => $pending,
    'total_selesai' => $selesai,
]);
?>
