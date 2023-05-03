import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../computer.test-samples';

import { ComputerFormService } from './computer-form.service';

describe('Computer Form Service', () => {
  let service: ComputerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ComputerFormService);
  });

  describe('Service methods', () => {
    describe('createComputerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createComputerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            macAddress: expect.any(Object),
            ipAddress: expect.any(Object),
            employeeAbbreviation: expect.any(Object),
            description: expect.any(Object),
          })
        );
      });

      it('passing IComputer should create a new form with FormGroup', () => {
        const formGroup = service.createComputerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            macAddress: expect.any(Object),
            ipAddress: expect.any(Object),
            employeeAbbreviation: expect.any(Object),
            description: expect.any(Object),
          })
        );
      });
    });

    describe('getComputer', () => {
      it('should return NewComputer for default Computer initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createComputerFormGroup(sampleWithNewData);

        const computer = service.getComputer(formGroup) as any;

        expect(computer).toMatchObject(sampleWithNewData);
      });

      it('should return NewComputer for empty Computer initial value', () => {
        const formGroup = service.createComputerFormGroup();

        const computer = service.getComputer(formGroup) as any;

        expect(computer).toMatchObject({});
      });

      it('should return IComputer', () => {
        const formGroup = service.createComputerFormGroup(sampleWithRequiredData);

        const computer = service.getComputer(formGroup) as any;

        expect(computer).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IComputer should not enable id FormControl', () => {
        const formGroup = service.createComputerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewComputer should disable id FormControl', () => {
        const formGroup = service.createComputerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
