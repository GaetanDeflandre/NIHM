library("reshape")
library("ez")

# Chargement des donnees
data = read.table("data.txt", header=TRUE, sep=",")

# On ne garde que ce qui nous interesse
filteredData = subset(data, (Err==0), select = c(Participant, Block, Technique,
                      A, W, density, Time))

# Aggregation des donnees pour ne conserver qu’une valeur par condition
attach(filteredData)
aggdata = aggregate(filteredData$Time, by=list(Participant,Block,Technique,W, density),
                    FUN=mean)
detach(filteredData)

# Reecriture des noms de colonnes
colnames(aggdata) = c("Participant","Block","Technique","W", "density", "Time")

# Conversion des donnees au format long
data.long = melt(aggdata, id = c("Participant","Block","Technique","W","density","Time"))

# On specifie les variables independantes
data.long$Block = factor(data.long$Block)
data.long$Technique = factor(data.long$Technique)
data.long$W = factor(data.long$W)
data.long$density = factor(data.long$density)

# L’ANOVA:
anova = ezANOVA(data.long, dv=.(Time), wid=.(Participant), within=.(Technique,W,density))
print(anova)

# Analyse post-hoc avec ajustement de Bonferroni
attach(data.long)
print(pairwise.t.test(Time, interaction(Technique), p.adj = "bonf"))
print(pairwise.t.test(Time, interaction(Technique, density), p.adj = "bonf"))
detach(data.long)
