import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPayment, NewPayment } from '../payment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPayment for edit and NewPaymentFormGroupInput for create.
 */
type PaymentFormGroupInput = IPayment | PartialWithRequiredKeyOf<NewPayment>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPayment | NewPayment> = Omit<T, 'placedDate'> & {
  placedDate?: string | null;
};

type PaymentFormRawValue = FormValueOf<IPayment>;

type NewPaymentFormRawValue = FormValueOf<NewPayment>;

type PaymentFormDefaults = Pick<NewPayment, 'id' | 'placedDate'>;

type PaymentFormGroupContent = {
  id: FormControl<PaymentFormRawValue['id'] | NewPayment['id']>;
  placedDate: FormControl<PaymentFormRawValue['placedDate']>;
  quantity: FormControl<PaymentFormRawValue['quantity']>;
  order: FormControl<PaymentFormRawValue['order']>;
  user: FormControl<PaymentFormRawValue['user']>;
  usercart: FormControl<PaymentFormRawValue['usercart']>;
};

export type PaymentFormGroup = FormGroup<PaymentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaymentFormService {
  createPaymentFormGroup(payment: PaymentFormGroupInput = { id: null }): PaymentFormGroup {
    const paymentRawValue = this.convertPaymentToPaymentRawValue({
      ...this.getFormDefaults(),
      ...payment,
    });
    return new FormGroup<PaymentFormGroupContent>({
      id: new FormControl(
        { value: paymentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      placedDate: new FormControl(paymentRawValue.placedDate),
      quantity: new FormControl(paymentRawValue.quantity),
      order: new FormControl(paymentRawValue.order),
      user: new FormControl(paymentRawValue.user),
      usercart: new FormControl(paymentRawValue.usercart),
    });
  }

  getPayment(form: PaymentFormGroup): IPayment | NewPayment {
    return this.convertPaymentRawValueToPayment(form.getRawValue() as PaymentFormRawValue | NewPaymentFormRawValue);
  }

  resetForm(form: PaymentFormGroup, payment: PaymentFormGroupInput): void {
    const paymentRawValue = this.convertPaymentToPaymentRawValue({ ...this.getFormDefaults(), ...payment });
    form.reset(
      {
        ...paymentRawValue,
        id: { value: paymentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PaymentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      placedDate: currentTime,
    };
  }

  private convertPaymentRawValueToPayment(rawPayment: PaymentFormRawValue | NewPaymentFormRawValue): IPayment | NewPayment {
    return {
      ...rawPayment,
      placedDate: dayjs(rawPayment.placedDate, DATE_TIME_FORMAT),
    };
  }

  private convertPaymentToPaymentRawValue(
    payment: IPayment | (Partial<NewPayment> & PaymentFormDefaults)
  ): PaymentFormRawValue | PartialWithRequiredKeyOf<NewPaymentFormRawValue> {
    return {
      ...payment,
      placedDate: payment.placedDate ? payment.placedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
