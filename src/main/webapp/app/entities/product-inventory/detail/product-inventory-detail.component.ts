import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductInventory } from '../product-inventory.model';

@Component({
  selector: 'jhi-product-inventory-detail',
  templateUrl: './product-inventory-detail.component.html',
})
export class ProductInventoryDetailComponent implements OnInit {
  productInventory: IProductInventory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productInventory }) => {
      this.productInventory = productInventory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
