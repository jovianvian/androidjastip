package com.example.jastiproops.api

import com.example.jastiproops.models.*
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("auth/login.php")
    fun login(@Field("email") email: String, @Field("password") password: String): Call<ApiResponse<LoginResponseData>>

    @FormUrlEncoded
    @POST("reports/dashboard_summary.php")
    fun dashboard(@Field("user_id") userId: Int): Call<ApiResponse<DashboardSummary>>

    @FormUrlEncoded
    @POST("customers/get_customers.php")
    fun getCustomers(@Field("user_id") userId: Int, @Field("keyword") keyword: String = ""): Call<ApiResponse<List<Customer>>>

    @FormUrlEncoded
    @POST("customers/create_customer.php")
    fun createCustomer(
        @Field("user_id") userId: Int,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("address") address: String,
        @Field("notes") notes: String
    ): Call<ApiResponse<IdResponse>>

    @FormUrlEncoded
    @POST("customers/update_customer.php")
    fun updateCustomer(
        @Field("user_id") userId: Int,
        @Field("id") id: Int,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("address") address: String,
        @Field("notes") notes: String
    ): Call<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("customers/delete_customer.php")
    fun deleteCustomer(@Field("user_id") userId: Int, @Field("id") id: Int): Call<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("products/get_products.php")
    fun getProducts(@Field("user_id") userId: Int, @Field("keyword") keyword: String = ""): Call<ApiResponse<List<Product>>>

    @FormUrlEncoded
    @POST("products/create_product.php")
    fun createProduct(
        @Field("user_id") userId: Int,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("price") price: Int,
        @Field("origin_city") originCity: String,
        @Field("eta_days") etaDays: Int,
        @Field("is_active") isActive: Int
    ): Call<ApiResponse<IdResponse>>

    @FormUrlEncoded
    @POST("products/update_product.php")
    fun updateProduct(
        @Field("user_id") userId: Int,
        @Field("id") id: Int,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("price") price: Int,
        @Field("origin_city") originCity: String,
        @Field("eta_days") etaDays: Int,
        @Field("is_active") isActive: Int
    ): Call<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("products/delete_product.php")
    fun deleteProduct(@Field("user_id") userId: Int, @Field("id") id: Int): Call<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("orders/get_orders.php")
    fun getOrders(@Field("user_id") userId: Int, @Field("status") status: String = ""): Call<ApiResponse<List<OrderItem>>>

    @FormUrlEncoded
    @POST("orders/create_order.php")
    fun createOrder(
        @Field("user_id") userId: Int,
        @Field("customer_id") customerId: Int,
        @Field("product_id") productId: Int,
        @Field("quantity") quantity: Int,
        @Field("notes") notes: String
    ): Call<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("orders/get_order_detail.php")
    fun getOrderDetail(@Field("user_id") userId: Int, @Field("order_id") orderId: Int): Call<ApiResponse<OrderDetail>>

    @FormUrlEncoded
    @POST("orders/update_order_status.php")
    fun updateOrderStatus(
        @Field("user_id") userId: Int,
        @Field("order_id") orderId: Int,
        @Field("status") status: String,
        @Field("note") note: String
    ): Call<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("users/get_staff.php")
    fun getStaff(@Field("user_id") userId: Int): Call<ApiResponse<List<User>>>

    @FormUrlEncoded
    @POST("users/create_staff.php")
    fun createStaff(
        @Field("user_id") userId: Int,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("is_active") isActive: Int
    ): Call<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("users/get_profile.php")
    fun getProfile(@Field("user_id") userId: Int): Call<ApiResponse<User>>

    @FormUrlEncoded
    @POST("users/update_profile.php")
    fun updateProfile(
        @Field("user_id") userId: Int,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Call<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("reports/simple_report.php")
    fun simpleReport(@Field("user_id") userId: Int): Call<ApiResponse<Map<String, Any>>>
}
