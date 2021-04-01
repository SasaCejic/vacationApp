# vacationApp


1. PODESAVANJE OKRUZENJA I POKRETANJE APLIKACIJE


- Instalirati razvojno okruzenje IntellijIdea. Moze se naci na sledecoj adresi:  https://www.jetbrains.com/idea/download/
- Za pokretanje ove aplikacije bice potrebno instalirati i SDK 11, sto se moze naci na: https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
- Otvoriti aplikaciju u IntellijIdea okruzenju


- ODABIRANJE PROJECT SDK:
- file > Project Structure > Project > Project SDK > odabrati instaliranu verziju SDK (11)

- ODABIRANJE SOURCE FOLDERA:
- file > Project Structure > Modules
- u tabu sources selektovati src folder i odabrati opciju iznad: Mark as sources


- DEFINISANJE OUTPUT PUTANJE:
- file > Project Structure > Modules
- u tabu paths unutar odeljka Compiler output selektovati Use module compile output path
- zatim pod Output path selektovati folder VacationApp unutar out\production\

- IMPORTOVANJE MAVEN BIBLIOTEKE ZA RAD SA JSON-OM:
- file > Project Structure > Modules > Libraries > + > From Maven
- u pretragu ukucati:  com.googlecode.json-simple:json-simple:1.1.1
- nakon importovanja ove biblioteke, aplikacija je spremna za pokretanje




2. TESTIRANJE APLIKACIJE


- LOGOVANJE KORISNIKA:
- Pri pokretanju aplikacije, treba napisati tip korisnika koji se prijavljuje (admin/employee).
- Nakon toga, unose se ime i prezime korisnika (videti u 'users.json' fajlu) i njegova lozinka (lozinka je ime korisnika malim slovima).


- UKOLIKO JE PRIJAVLJENI KORISNIK ADMIN:
- Ukoliko ih ima ujson fajlu, prikazuju se zahtevi za odmor ciji je status aktivan (koji jos uvek nisu odobreni ili nisu istekli), tj. prikazuje se datum pocetka odmora
u zahtevu, datum kraja odmora i id korisnika koje je poslao zahtev.
- zatim se od admina trazi da unese id korisnika ciji status zeli da odobri ili odbije
- prikazuju se dve opcije: 
  1. za odobravanje zahteva
  2. za odbijanje zahteva
- ukoliko admin unese 1, status zahteva u json fajlu se menja u 'approved'
- ukoliko se unese 2, status zahteva u json fajlu se menja u 'denied'


- UKOLIKO JE PRIJAVLJENI KORISNIK ZAPOSLENI (EMPLOYEE):
- najpre treba da unese datum pocetka odmora za koji zeli da podnese zahtev
- zatim unosi datum kraja odmora
- nakon toga program proverava da li postoje svi uslovi da se zahtev kreira i kreira ga i upisuje u json fajl sa statusom 'active' ukoliko
 su ispunjeni svi uslovi.
