<?php
require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../config/response.php';
require_once __DIR__ . '/../config/auth.php';

getAuthUser();
$keyword = input('keyword');
$pdo = getPDO();

$sql = 'SELECT id, name, phone, address, notes, created_at FROM customers WHERE is_active=1';
$params = [];
if ($keyword !== '') {
    $sql .= ' AND (name LIKE ? OR phone LIKE ?)';
    $params[] = "%$keyword%";
    $params[] = "%$keyword%";
}
$sql .= ' ORDER BY id DESC';
$stmt = $pdo->prepare($sql);
$stmt->execute($params);
jsonResponse(true, 'Daftar customer', $stmt->fetchAll());
?>
