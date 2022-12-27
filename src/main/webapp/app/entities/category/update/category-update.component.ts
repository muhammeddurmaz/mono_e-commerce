import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CategoryFormService, CategoryFormGroup } from './category-form.service';
import { ICategory } from '../category.model';
import { CategoryService } from '../service/category.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IProductType } from 'app/entities/product-type/product-type.model';
import { ProductTypeService } from 'app/entities/product-type/service/product-type.service';

@Component({
  selector: 'jhi-category-update',
  templateUrl: './category-update.component.html',
})
export class CategoryUpdateComponent implements OnInit {
  isSaving = false;
  category: ICategory | null = null;

  productTypesSharedCollection: IProductType[] = [];

  editForm: CategoryFormGroup = this.categoryFormService.createCategoryFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected categoryService: CategoryService,
    protected categoryFormService: CategoryFormService,
    protected productTypeService: ProductTypeService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProductType = (o1: IProductType | null, o2: IProductType | null): boolean => this.productTypeService.compareProductType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ category }) => {
      this.category = category;
      if (category) {
        this.updateForm(category);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('fastShopApp.error', { message: err.message })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const category = this.categoryFormService.getCategory(this.editForm);
    if (category.id !== null) {
      this.subscribeToSaveResponse(this.categoryService.update(category));
    } else {
      this.subscribeToSaveResponse(this.categoryService.create(category));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategory>>): void {
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

  protected updateForm(category: ICategory): void {
    this.category = category;
    this.categoryFormService.resetForm(this.editForm, category);

    this.productTypesSharedCollection = this.productTypeService.addProductTypeToCollectionIfMissing<IProductType>(
      this.productTypesSharedCollection,
      category.type
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productTypeService
      .query()
      .pipe(map((res: HttpResponse<IProductType[]>) => res.body ?? []))
      .pipe(
        map((productTypes: IProductType[]) =>
          this.productTypeService.addProductTypeToCollectionIfMissing<IProductType>(productTypes, this.category?.type)
        )
      )
      .subscribe((productTypes: IProductType[]) => (this.productTypesSharedCollection = productTypes));
  }
}
