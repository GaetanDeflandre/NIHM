#
# TP Introduction à R
# @author Gaetan DEFLANDRE
#

library("gplots")


# =========
# FUNCTIONS
# =========
meanTimeOfTech = function(data, techName){
  technique = subset(data, Technique==techName)
  meanTime = mean(technique[,"Time"])
  return (meanTime)
}

confInterOfTech = function (data, techName){
    technique = subset(data, Technique==techName)
    SD = sd(technique[,"Time"])
    N = length(technique[,"Time"])
    return (1.96 * (SD/sqrt(N)))
}

meanTimeOfTechByDensity = function(data, techniques, densVal){
    dens = subset(data, density=densVal)
    return (sapply(techniques, meanTimeOfTech, data=dens))
}



# ======
# SCRIPT
# ======


# 3. Premiers pas
# ---------------

# Question 1
# data
v = c(12, .4, 5, 2, 50, 8, 3, 1, 4, .25)

p90e  = quantile(v, .90)


# 4. Graphiques
# -------------

# Question 2
#pdf(file="test.pdf")

#boxplot(v)
#barplot(v)

#dev.off()



# 5. Importation de données à partir de fichiers
# ----------------------------------------------

# exemple function 'names' :
# 'names(data[1])' return "Participant"

# Question 3
data = read.table("data.txt", header=TRUE, sep=",")
participant2SurfPad = subset(data,Participant==2 & Technique=="SurfPad")

# Question 6
dataSsErr = subset(data,Err==0)


techMean = mean(participant2SurfPad[,"Time"])
techniques = unique(data$Technique)
# Pour la question 10
#techniques = techniques[-2]

# Question 4
meansByTech = sapply(techniques, meanTimeOfTech, data=dataSsErr)

# Question 5
# barplot(meansByTech, names.arg=techniques, main="Temps moyen par technique")

# Question 7
CIsByTech = sapply(techniques, confInterOfTech, data=dataSsErr)

# Question 8
lowerTechCIs = meansByTech - CIsByTech/2
upperTechCIs = meansByTech + CIsByTech/2
#barplot2(meansByTech, names.arg=techniques,
#    main="Temps moyen par technique avec intervalle de confiance",
#    plot.ci=TRUE, ci.l=lowerCIs, ci.u=upperCIs)


# Question 9
# Voir README.md

# Question 10
densities = unique(data$density)
meansByDens = sapply(densities, meanTimeOfTechByDensity,
    data=dataSsErr, techniques=techniques)
barplot(meansByDens, names.arg=densities, beside=TRUE,
    xlab="Densité", ylab="Temps moyen",
    main="Temps moyen des techniques par densité")
