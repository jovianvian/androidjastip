<?php
require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../config/response.php';
require_once __DIR__ . '/../config/auth.php';

$user = getAuthUser();
requireManager($user);
$pdo = getPDO();

$summary = [];
$summary['orders_per_status'] = $pdo->query('SELECT status, COUNT(*) total FROM orders GROUP BY status ORDER BY FIELD(status, "pending","diproses","dibelikan","dikirim","selesai","dibatalkan")')->fetchAll();
$summary['orders_today'] = (int)$pdo->query('SELECT COUNT(*) FROM orders WHERE DATE(created_at)=CURDATE()')->fetchColumn();
$summary['revenue_selesai'] = (int)$pdo->query("SELECT COALESCE(SUM(total_amount),0) FROM orders WHERE status='selesai'")->fetchColumn();
jsonResponse(true, 'Laporan sederhana', $summary);
?>
