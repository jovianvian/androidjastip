<?php
require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../config/response.php';
require_once __DIR__ . '/../config/auth.php';

getAuthUser();
$name = input('name');
$phone = input('phone');
$address = input('address');
$notes = input('notes');

if ($name === '' || $phone === '' || $address === '') {
    jsonResponse(false, 'Nama, no hp, alamat wajib diisi', null, 422);
}

$pdo = getPDO();
$stmt = $pdo->prepare('SELECT id FROM customers WHERE phone=? AND is_active=1 LIMIT 1');
$stmt->execute([$phone]);
if ($stmt->fetch()) {
    jsonResponse(false, 'No HP customer sudah terdaftar', null, 409);
}

$stmt = $pdo->prepare('INSERT INTO customers (name, phone, address, notes, is_active, created_at, updated_at) VALUES (?, ?, ?, ?, 1, NOW(), NOW())');
$stmt->execute([$name, $phone, $address, $notes]);
jsonResponse(true, 'Customer berhasil ditambahkan', ['id' => (int)$pdo->lastInsertId()]);
?>
