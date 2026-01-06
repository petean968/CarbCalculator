Official Store: Google Play Store - CarbCalculator:
https://play.google.com/store/apps/details?id=com.peteanionelsorin.carbcalculator

The main application logic is handled in MainActivity.kt (UI layout defined via XML):
https://github.com/petean968/CarbCalculator/blob/main/selapela/src/main/java/com/example/carbcalculator/MainActivity.kt


CarbCalculator — My First Android Application

This is my very first project developed in Android Studio using Kotlin.

Project Origin The project started as a learning exercise based on a "Tip Calculator" tutorial. 
While the mathematical structure is similar, I refactored and adapted the entire logic to serve 
a much more practical need: rapid carbohydrate calculation for people who need to rigorously 
monitor their nutrition (e.g., diabetics or athletes).

What I learned while building this project: UI/UX Design: I customized the visual elements to fit 
the nutritional context. Android Components: Using EditText for input, SeekBar for percentage 
selection, and TextView for displaying real-time results. Kotlin Logic: Implementing a reactive 
calculation system that updates instantly whenever the input changes. Data Safety: Handling input 
errors (empty fields or invalid characters) to prevent the app from crashing.

How it works:

The user enters the food weight in grams.

The user selects the carbohydrate percentage for that specific food using the slider (SeekBar).

The application instantly displays the net amount of carbohydrates to be consumed.

CarbCalculator - Prima mea aplicație Android

Acesta este primul meu proiect dezvoltat în Android Studio folosind Kotlin.

 Originea Proiectului
Proiectul a pornit ca un exercițiu de învățare bazat pe un tutorial de tip "Tip Calculator" 
(Calculator de Bacșiș), vazut pe YouTube. 
Deși structura matematică este similară, am ales să refactorizez și să adaptez întreaga logică 
pentru a servi unei nevoi mult mai practice: calculul rapid al carbohidraților pentru persoanele care 
trebuie să monitorizeze riguros nutriția (ex: diabetici sau sportivi).

 Ce am învățat realizând acest proiect:
    UI/UX Design: Am personalizat elementele vizuale pentru a se potrivi contextului nutrițional.
    Componente Android: Utilizarea EditText_pentru input, SeekBar_pentru selectarea procentelor și
    TextView_pentru afișarea rezultatelor în timp real.
    Logică în Kotlin: Implementarea unui sistem de calcul reactiv care se actualizează instantaneu
    la orice modificare a inputului.
    Siguranța datelor: Gestionarea erorilor de input (câmpuri goale sau caractere invalide) pentru 
     a preveni închiderea aplicației (crash).

 Cum funcționează:
    Utilizatorul introduce greutatea alimentului în grame.
    Selectează procentul de carbohidrați specific acelui aliment folosind slider-ul (SeekBar).
    Aplicația afișează instantaneu cantitatea netă de glucide ce urmează a fi consumată.


