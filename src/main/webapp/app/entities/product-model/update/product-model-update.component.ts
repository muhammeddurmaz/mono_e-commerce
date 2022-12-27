import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ProductModelFormService, ProductModelFormGroup } from './product-model-form.service';
import { IProductModel } from '../product-model.model';
import { ProductModelService } from '../service/product-model.service';

@Component({
  selector: 'jhi-product-model-update',
  templateUrl: './product-model-update.component.html',
})
export class ProductModelUpdateComponent implements OnInit {
  isSaving = false;
  productModel: IProductModel | null = null;

  editForm: ProductModelFormGroup = this.productModelFormService.createProductModelFormGroup();

  constructor(
    protected productModelService: ProductModelService,
    protected productModelFormService: ProductModelFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productModel }) => {
      this.productModel = productModel;
      if (productModel) {
        this.updateForm(productModel);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productModel = this.productModelFormService.getProductModel(this.editForm);
    if (productModel.id !== null) {
      this.subscribeToSaveResponse(this.productModelService.update(productModel));
    } else {
      this.subscribeToSaveResponse(this.productModelService.create(productModel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductModel>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(productModel: IProductModel): void {
    this.productModel = productModel;
    this.productModelFormService.resetForm(this.editForm, productModel);
  }
}
