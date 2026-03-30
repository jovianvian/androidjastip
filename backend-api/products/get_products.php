<?php
require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../config/response.php';
require_once __DIR__ . '/../config/auth.php';

getAuthUser();
$keyword = input('keyword');
$pdo = getPDO();
$sql = 'SELECT id, name, description, price, origin_city, eta_days, is_active, created_at FROM products WHERE is_active=1';
$params = [];
if ($keyword !== '') {
    $sql .= ' AND name LIKE ?';
    $params[] = "%$keyword%";
}
$sql .= ' ORDER BY id DESC';
$stmt = $pdo->prepare($sql);
$stmt->execute($params);
jsonResponse(true, 'Daftar produk', $stmt->fetchAll());
?>
