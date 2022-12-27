import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BrandFormService, BrandFormGroup } from './brand-form.service';
import { IBrand } from '../brand.model';
import { BrandService } from '../service/brand.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ISeller } from 'app/entities/seller/seller.model';
import { SellerService } from 'app/entities/seller/service/seller.service';

@Component({
  selector: 'jhi-brand-update',
  templateUrl: './brand-update.component.html',
})
export class BrandUpdateComponent implements OnInit {
  isSaving = false;
  brand: IBrand | null = null;

  sellersCollection: ISeller[] = [];

  editForm: BrandFormGroup = this.brandFormService.createBrandFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected brandService: BrandService,
    protected brandFormService: BrandFormService,
    protected sellerService: SellerService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSeller = (o1: ISeller | null, o2: ISeller | null): boolean => this.sellerService.compareSeller(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ brand }) => {
      this.brand = brand;
      if (brand) {
        this.updateForm(brand);
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
    const brand = this.brandFormService.getBrand(this.editForm);
    if (brand.id !== null) {
      this.subscribeToSaveResponse(this.brandService.update(brand));
    } else {
      this.subscribeToSaveResponse(this.brandService.create(brand));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBrand>>): void {
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

  protected updateForm(brand: IBrand): void {
    this.brand = brand;
    this.brandFormService.resetForm(this.editForm, brand);

    this.sellersCollection = this.sellerService.addSellerToCollectionIfMissing<ISeller>(this.sellersCollection, brand.seller);
  }

  protected loadRelationshipsOptions(): void {
    this.sellerService
      .query({ 'brandId.specified': 'false' })
      .pipe(map((res: HttpResponse<ISeller[]>) => res.body ?? []))
      .pipe(map((sellers: ISeller[]) => this.sellerService.addSellerToCollectionIfMissing<ISeller>(sellers, this.brand?.seller)))
      .subscribe((sellers: ISeller[]) => (this.sellersCollection = sellers));
  }
}
