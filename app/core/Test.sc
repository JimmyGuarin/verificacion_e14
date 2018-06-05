val t = List(
  (1, "a", 5),
  (2, "a", 5),
  (3, "a", 5),
  (4, "a", 10),
  (5, "a", 20),
  (6, "a", 24),
  (7, "b", 7),
  (8, "c", 7),
  (9, "a", 5),
  (10, "a", 10),
)

val res = t.groupBy(t => (t._2, t._3))

val groupByCandidato = t.groupBy(tu => tu._2)

val groupByCandidatoAndVotos = groupByCandidato.map{case(k, s) =>
  (k, s.groupBy(_._3))
}

val groupByCandidatoAndVotosConSize = groupByCandidatoAndVotos.map{ case (k, v) =>
  (k, v.map{case (kk, vv) => (kk, vv.size)})
}
