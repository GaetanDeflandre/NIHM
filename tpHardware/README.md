TP Hardware
===========


## Timer

Fréquence timer:
    F_T = fréquance timer
    F_T = 90 MHz

Timer de 16 bits pour encoder le counter:
    taille_max = 2^^16

Fréquence clignontement de la diode:
    F = F_T / (taille * prescale)
    pescale = F_T / (taille * F)
    taille = (90*10^^6) / (256*10)
	
	
