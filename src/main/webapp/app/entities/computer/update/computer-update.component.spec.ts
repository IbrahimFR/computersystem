import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ComputerFormService } from './computer-form.service';
import { ComputerService } from '../service/computer.service';
import { IComputer } from '../computer.model';

import { ComputerUpdateComponent } from './computer-update.component';

describe('Computer Management Update Component', () => {
  let comp: ComputerUpdateComponent;
  let fixture: ComponentFixture<ComputerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let computerFormService: ComputerFormService;
  let computerService: ComputerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ComputerUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ComputerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ComputerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    computerFormService = TestBed.inject(ComputerFormService);
    computerService = TestBed.inject(ComputerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const computer: IComputer = { id: 456 };

      activatedRoute.data = of({ computer });
      comp.ngOnInit();

      expect(comp.computer).toEqual(computer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IComputer>>();
      const computer = { id: 123 };
      jest.spyOn(computerFormService, 'getComputer').mockReturnValue(computer);
      jest.spyOn(computerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ computer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: computer }));
      saveSubject.complete();

      // THEN
      expect(computerFormService.getComputer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(computerService.update).toHaveBeenCalledWith(expect.objectContaining(computer));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IComputer>>();
      const computer = { id: 123 };
      jest.spyOn(computerFormService, 'getComputer').mockReturnValue({ id: null });
      jest.spyOn(computerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ computer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: computer }));
      saveSubject.complete();

      // THEN
      expect(computerFormService.getComputer).toHaveBeenCalled();
      expect(computerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IComputer>>();
      const computer = { id: 123 };
      jest.spyOn(computerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ computer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(computerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
