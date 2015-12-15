#
# FUNCTIONS
#
meanGroupByTech = function(data, techName){
  groupedTech = subset(data, Technique==techName)
  groupedMean = mean(groupedTech[,"Time"])
  return groupedMean
}


#
# SCRIPT
#

# Question 1

# data
v = c(12, .4, 5, 2, 50, 8, 3, 1, 4, .25)

p90e  = quantile(v, .90)


# Question 2
pdf(file="test.pdf")

boxplot(v)
barplot(v)

dev.off()

# Question 3
data = read.table("data.txt", header=TRUE, sep=",")
participant2SurfPad = subset(data,Participant==2 & Technique=="SurfPad")

techMean = mean(participant2SurfPad[,"Time"])
techniques = unique(data$Technique)

# Question 4
# Voir le fonctionnement de cette fonction 
sapply(techniques, meanGroupByTech, dataFrame=data)
