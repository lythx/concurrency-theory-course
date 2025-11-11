from string import ascii_lowercase


class Equation:
    def __init__(self, name: str, left: str = None, right: set[str] = None):
        self.name = name
        self.left = left
        self.right = right


def load_data(filename: str) -> str:
    with open(filename, "r") as file:
        data = file.read()
    return data


def parse_data(data: str) -> tuple[dict[str, Equation], str]:
    lines = data.splitlines()
    equations: dict[str, Equation] = {}

    set_line = next(l for l in lines if l.startswith("A = {"))
    for char in [x.strip() for x in set_line[5:-1].split(",")]:
        equations[char] = Equation(name=char)

    for line in [l for l in lines if l[0] == "("]:
        closing_parantheses_split = line.split(") ")
        vertex_name = closing_parantheses_split[0][1]
        equal_split = closing_parantheses_split[1].split(" := ")
        left = equal_split[0].strip()
        right_split = equal_split[1].split(" ")
        right = set()
        for component in right_split:
            if component[-1] in ascii_lowercase:
                right.add(component[-1])
        equations[vertex_name].left = left
        equations[vertex_name].right = right

    word_line = next(l for l in lines if l.startswith("w = "))
    word = word_line[4:]
    return equations, word


def compute_D_I(
    equations: dict[str, Equation],
) -> tuple[set[tuple[str, str]], set[tuple[str, str]]]:
    D = set()
    I = set()
    for eq1 in equations.values():
        for eq2 in equations.values():
            if eq1.left == eq2.left or eq1.left in eq2.right or eq2.left in eq1.right:
                D.add((eq1.name, eq2.name))
            else:
                I.add((eq1.name, eq2.name))
    return D, I


def compute_dependency_graph(word: str, D: set[tuple[str, str]]) -> dict[str, set[str]]:
    dependency_graph: dict[int, set[int]] = {}
    for i, char1 in enumerate(word):
        dependency_graph[i] = set()
        for j, char2 in enumerate(word[i + 1 :], start=i + 1):
            if (char1, char2) in D:
                dependency_graph[i].add(j)
                # TODO remove unneeded edges
    return dependency_graph


def compute_FNF(word: str, dependency_graph: dict[int, set[int]]) -> list[set[str]]:
    fnf: list[set[str]] = []
    computed_chars: set[int] = set()
    for i, char in enumerate(word):
        if i in computed_chars:
            continue
        fnf.append(set([char]))
        computed_chars.add(i)
        dependant_chars = dependant_chars_dfs(i, dependency_graph)
        for j in range(i + 1, len(word)):
            if j not in dependant_chars:
                fnf[-1].add(
                    word[j]
                )  # TODO need dependant_chars_dfs for j, will require recursion
                computed_chars.add(j)
    return fnf


def dependant_chars_dfs(
    node: int,
    dependency_graph: dict[int, set[int]],
) -> set[int]:
    for neighbor in dependency_graph[node]:
        return {node} | dependant_chars_dfs(neighbor, dependency_graph)


if __name__ == "__main__":
    data = load_data("case1.txt")
    equations, word = parse_data(data)
    D, I = compute_D_I(equations)
    dependency_graph = compute_dependency_graph(word, D)

    print(D)
    print(I)
    print(dependency_graph)
