import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ComputerDetailComponent } from './computer-detail.component';

describe('Computer Management Detail Component', () => {
  let comp: ComputerDetailComponent;
  let fixture: ComponentFixture<ComputerDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ComputerDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ computer: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ComputerDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ComputerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load computer on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.computer).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
