package com.example.tikimvvm.models

data class TikiModel(
        val `data`: Data?,
        val status: Int?
)

data class Data(
        val `data`: List<DataX>?,
        val meta_data: MetaData?,
        val next_page: String?
)

data class DataX(
        val badges: List<Badge>?,
        val badges_new: List<BadgesNew>?,
        val brand_name: String?,
        val bundle_deal: Boolean?,
        val discount: Int?,
        val discount_rate: Int?,
        val favourite_count: Int?,
        val has_ebook: Boolean?,
        val id: Int?,
        val inventory: Inventory?,
        val inventory_status: String?,
        val is_flower: Boolean?,
        val is_gift_card: Boolean?,
        val list_price: Int?,
        val name: String?,
        val order_count: Int?,
        val original_price: Int?,
        val price: Int?,
        val productset_id: Int?,
        val quantity_sold: QuantitySold?,
        val rating_average: Double?,
        val review_count: Int?,
        val salable_type: String?,
        val score: Double?,
        val seller_product_id: Int?,
        val short_description: String?,
        val sku: String?,
        val stock_item: StockItem?,
        val thumbnail_height: Int?,
        val thumbnail_url: String?,
        val thumbnail_width: Int?,
        val type: String?,
        val url_attendant_input_form: String?,
        val url_key: String?,
        val url_path: String?,
        val video_url: String?
)

data class MetaData(
        val background_image: String?,
        val items: List<Item>?,
        val more_link: String?,
        val more_link_text: String?,
        val sub_title: String?,
        val title: String?,
        val title_icon: TitleIcon?,
        val type: String?
)

data class Badge(
        val code: String?,
        val text: String?
)

data class BadgesNew(
        val code: String?,
        val icon: String?,
        val icon_height: Int?,
        val icon_width: Int?,
        val placement: String?,
        val text: String?,
        val text_color: String?,
        val type: String?
)

data class Inventory(
        val fulfillment_type: String?
)

data class QuantitySold(
        val text: String?,
        val value: Int?
)

data class StockItem(
        val max_sale_qty: Int?,
        val min_sale_qty: Int?,
        val preorder_date: Boolean?,
        val qty: Int?
)

data class Item(
        val category_id: Int?,
        val images: List<String>,
        val title: String?
)

class TitleIcon