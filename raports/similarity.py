import numpy as np


def nodes_similarity(solution1, solution2):
    ''' this function assumes solutions are of the same length'''
    return len(set(solution1['cityOrder']) & set(solution2['cityOrder'])) / len(solution1['cityOrder'])


def get_edges_zip(city_order):
    return zip(city_order, city_order[1:]+[city_order[0]])


def get_bidirectional_edges(city_order):
    return map(lambda edge: (min(edge), max(edge)), get_edges_zip(city_order))


def edges_similarity(solution1, solution2):
    ''' this function assumes solutions are of the same length'''
    return len(set(solution1['edges']) & set(solution2['edges'])) / len(solution1['edges'])


def add_edges(solutions):
    for solution in solutions:
        solution["edges"] = set(get_bidirectional_edges(solution['cityOrder']))


def similarity_matrix(solutions, instance, similarity):
    instance_solutions = list(filter(lambda s: s['instance'] == instance, solutions))
    simils = np.zeros((len(instance_solutions), len(instance_solutions)))
    for i, solution1 in enumerate(instance_solutions):
        for j, solution2 in enumerate(instance_solutions[i:], i):
            simil = similarity(solution1, solution2) if i != j else 1.0
            simils[i, j] = simil
            simils[j, i] = simil

    return simils


def similarity_to_best(solutions, instance, similarity_matrix):
    instance_solutions = list(filter(lambda s: s['instance']==instance, solutions))
    best_i, best_solution = min(enumerate(instance_solutions), key=lambda i_s: i_s[1]['objective function'])

    return np.array([(solution['objective function'], simil) for solution, simil in zip(instance_solutions,similarity_matrix[:, best_i]) if solution != best_solution])


def similarity_average(solutions, instance, similarity_matrix):
    instance_solutions = list(filter(lambda s: s['instance']==instance, solutions))
    return np.array([(solution['objective function'], simil) for solution, simil in zip(instance_solutions, np.average(similarity_matrix, axis=1))])
