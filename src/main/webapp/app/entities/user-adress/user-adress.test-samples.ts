import { IUserAdress, NewUserAdress } from './user-adress.model';

export const sampleWithRequiredData: IUserAdress = {
  id: 40994,
  name: 'Drive',
  lastName: 'Durmaz',
  telephone: '+90-219-175-0-992',
  city: 'Alkanberg',
  adressTitle: 'Keyboard',
};

export const sampleWithPartialData: IUserAdress = {
  id: 1562,
  name: 'Branding',
  lastName: 'Toraman',
  telephone: '+90-110-050-02-41',
  city: 'South Bardıbay',
  adressTitle: 'Rial orange',
};

export const sampleWithFullData: IUserAdress = {
  id: 44538,
  name: 'Proactive Light',
  lastName: 'Ayverdi',
  telephone: '+90-258-335-0-531',
  city: 'Ozansoyton',
  adress: '../fake-data/blob/hipster.txt',
  adressTitle: 'red',
};

export const sampleWithNewData: NewUserAdress = {
  name: 'Missouri AGP PCI',
  lastName: 'Arslanoğlu',
  telephone: '+90-843-665-8-755',
  city: 'Elçiboğafort',
  adressTitle: 'payment hybrid',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
