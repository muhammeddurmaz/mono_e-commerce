import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductModel } from '../product-model.model';

@Component({
  selector: 'jhi-product-model-detail',
  templateUrl: './product-model-detail.component.html',
})
export class ProductModelDetailComponent implements OnInit {
  productModel: IProductModel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productModel }) => {
      this.productModel = productModel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
