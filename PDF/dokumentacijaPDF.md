# Java Swing aplikacija s MVC strukturom, koristeći Observer i Strategy Pattern

## Opis
Ova aplikacija simulira TriCount aplikaciju. Glavni cilj je stvoriti jednostavnu aplikaciju koja omogućuje dijeljenje računa među prijateljima. Aplikacija omogućuje korisnicima dodavanje prijatelja, dodavanje troškova i dijeljenje računa među prijateljima.

## Tehnologije
- Java
- Swing
- Baza podataka
- MVC (Model-View-Controller)
- Observer Pattern
- Strategy Pattern

## Struktura projekta

### Model
Paket `Model` sadrži klase koje predstavljaju podatke i poslovnu logiku aplikacije.

#### Klase
- **Expense**: Predstavlja opći trošak.
- **AccommodationExpenses**: Predstavlja troškove smještaja, nasljeđuje klasu `Expense`.
- **ExpencesModel**: Upravljanje popisom troškova i obavještavanje promatrača o promjenama.
- **ExpencesObservable**: Sučelje za promatranje troškova.
- **ExpencesObserver**: Sučelje za promatrače troškova.

### View
Paket `View` sadrži klase koje predstavljaju korisničko sučelje aplikacije.

#### Klase
- **MenuBar**: Predstavlja traku izbornika s opcijama za izvoz, uvoz i brisanje podataka.
- **LeftPanel**: Predstavlja lijevu stranu panela gdje korisnici mogu unositi podatke o troškovima.
- **RightPanel**: Predstavlja desnu stranu panela gdje se prikazuju troškovi.
- **MenuBarEvent**: Predstavlja događaje pokrenute trakom izbornika.
- **MenuBarListener**: Sučelje za slušanje događaja trake izbornika.

### Controller
Paket `Controller` sadrži klase koje upravljaju interakcijom između modela i pogleda.

#### Klase
- **Controller**: Upravljanje komunikacijom između modela i pogleda, obrada događaja i rukovanje uvozom/izvozom podataka.

## Korišteni obrasci

### MVC (Model-View-Controller)
- **Model**: Upravljanje podacima i poslovnom logikom.
- **View**: Prikaz podataka i slanje korisničkih akcija kontroleru.
- **Controller**: Obrada korisničkog unosa, ažuriranje modela i osvježavanje pogleda.

### Observer Pattern
- **ExpencesObservable**: Sučelje za dodavanje i obavještavanje promatrača.
- **ExpencesObserver**: Sučelje za ažuriranje promatrača kada se model promijeni.

### Strategy Pattern
- **Expense**: Osnovna klasa za različite vrste troškova.
- **AccommodationExpenses**: Specifična implementacija troška.

## Korištenje
1. **Dodavanje korisnika**: Koristite lijevi panel za dodavanje prijatelja.
2. **Dodavanje troškova**: Unesite podatke o troškovima u lijevi panel i pošaljite.
3. **Dijeljenje računa**: Desni panel prikazuje podijeljene troškove.
4. **Izvoz podataka**: Koristite traku izbornika za izvoz podataka u CSV, tekstualni ili binarni format.
5. **Uvoz podataka**: Koristite traku izbornika za uvoz podataka iz datoteke.
6. **Brisanje svih podataka iz baze podataka**: Koristite traku izbornika za brisanje svih podataka.


## Zaključak
Ova aplikacija pruža jednostavan i učinkovit način za upravljanje i dijeljenje troškova među prijateljima koristeći dobro strukturiranu MVC arhitekturu i dizajnerske obrasce.