<?php
require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../config/response.php';
require_once __DIR__ . '/../config/auth.php';

getAuthUser();
$name = input('name');
$description = input('description');
$price = (int)input('price', 0);
$originCity = input('origin_city');
$etaDays = (int)input('eta_days', 0);
$isActive = (int)input('is_active', 1) === 1 ? 1 : 0;

if ($name === '' || $price <= 0 || $originCity === '' || $etaDays <= 0) {
    jsonResponse(false, 'Data produk tidak valid', null, 422);
}

$pdo = getPDO();
$stmt = $pdo->prepare('INSERT INTO products (name, description, price, origin_city, eta_days, is_active, created_at, updated_at) VALUES (?,?,?,?,?,?,NOW(),NOW())');
$stmt->execute([$name, $description, $price, $originCity, $etaDays, $isActive]);
jsonResponse(true, 'Produk berhasil ditambahkan', ['id' => (int)$pdo->lastInsertId()]);
?>
