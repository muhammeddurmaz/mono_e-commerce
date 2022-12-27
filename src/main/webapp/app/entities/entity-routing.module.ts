import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'seller',
        data: { pageTitle: 'Sellers' },
        loadChildren: () => import('./seller/seller.module').then(m => m.SellerModule),
      },
      {
        path: 'product-model',
        data: { pageTitle: 'ProductModels' },
        loadChildren: () => import('./product-model/product-model.module').then(m => m.ProductModelModule),
      },
      {
        path: 'product',
        data: { pageTitle: 'Products' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'property',
        data: { pageTitle: 'Properties' },
        loadChildren: () => import('./property/property.module').then(m => m.PropertyModule),
      },
      {
        path: 'property-des',
        data: { pageTitle: 'PropertyDes' },
        loadChildren: () => import('./property-des/property-des.module').then(m => m.PropertyDesModule),
      },
      {
        path: 'product-image',
        data: { pageTitle: 'ProductImages' },
        loadChildren: () => import('./product-image/product-image.module').then(m => m.ProductImageModule),
      },
      {
        path: 'color',
        data: { pageTitle: 'Colors' },
        loadChildren: () => import('./color/color.module').then(m => m.ColorModule),
      },
      {
        path: 'product-inventory',
        data: { pageTitle: 'ProductInventories' },
        loadChildren: () => import('./product-inventory/product-inventory.module').then(m => m.ProductInventoryModule),
      },
      {
        path: 'brand',
        data: { pageTitle: 'Brands' },
        loadChildren: () => import('./brand/brand.module').then(m => m.BrandModule),
      },
      {
        path: 'category',
        data: { pageTitle: 'Categories' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'sub-category',
        data: { pageTitle: 'SubCategories' },
        loadChildren: () => import('./sub-category/sub-category.module').then(m => m.SubCategoryModule),
      },
      {
        path: 'product-type',
        data: { pageTitle: 'ProductTypes' },
        loadChildren: () => import('./product-type/product-type.module').then(m => m.ProductTypeModule),
      },
      {
        path: 'discount',
        data: { pageTitle: 'Discounts' },
        loadChildren: () => import('./discount/discount.module').then(m => m.DiscountModule),
      },
      {
        path: 'product-discount',
        data: { pageTitle: 'ProductDiscounts' },
        loadChildren: () => import('./product-discount/product-discount.module').then(m => m.ProductDiscountModule),
      },
      {
        path: 'order',
        data: { pageTitle: 'Orders' },
        loadChildren: () => import('./order/order.module').then(m => m.OrderModule),
      },
      {
        path: 'order-item',
        data: { pageTitle: 'OrderItems' },
        loadChildren: () => import('./order-item/order-item.module').then(m => m.OrderItemModule),
      },
      {
        path: 'payment',
        data: { pageTitle: 'Payments' },
        loadChildren: () => import('./payment/payment.module').then(m => m.PaymentModule),
      },
      {
        path: 'user-adress',
        data: { pageTitle: 'UserAdresses' },
        loadChildren: () => import('./user-adress/user-adress.module').then(m => m.UserAdressModule),
      },
      {
        path: 'user-cart',
        data: { pageTitle: 'UserCarts' },
        loadChildren: () => import('./user-cart/user-cart.module').then(m => m.UserCartModule),
      },
      {
        path: 'favorite',
        data: { pageTitle: 'Favorites' },
        loadChildren: () => import('./favorite/favorite.module').then(m => m.FavoriteModule),
      },
      {
        path: 'comment',
        data: { pageTitle: 'Comments' },
        loadChildren: () => import('./comment/comment.module').then(m => m.CommentModule),
      },
      {
        path: 'product-statistics',
        data: { pageTitle: 'ProductStatistics' },
        loadChildren: () => import('./product-statistics/product-statistics.module').then(m => m.ProductStatisticsModule),
      },
      {
        path: 'seller-statistics',
        data: { pageTitle: 'SellerStatistics' },
        loadChildren: () => import('./seller-statistics/seller-statistics.module').then(m => m.SellerStatisticsModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
