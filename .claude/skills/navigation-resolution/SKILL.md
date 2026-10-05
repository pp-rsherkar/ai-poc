---
name: navigation-resolution
description: Resolve deterministic application navigation paths for Gherkin design from navigation-map/navigation-tree.html. Use when scenarios need repository-grounded page entry steps or framework-gap classification.
---

# Navigation Resolution

Use the repository's navigation graph as the authoritative source for application click paths. Do not invent a route for a page represented by the graph.

## Source

Read `navigation-map/navigation-tree.html` once per run. Extract only the object assigned to `const GRAPH = { ... };` inside its script block. Ignore rendering code, styles, legends, and the module-color map.

Parse the object literal and retain each node's module, landing state, mega-menu state, framework-gap flag, and outgoing actions.

## Resolution procedure

1. Build a forward adjacency map of `source -> [{target, action}]` and a reverse target index.
2. Record each module's `landing: true` node and the `MegaMenu` node.
3. Normalize the target area from Netra's ticket intent and `navigationContext`: case-fold and remove generic words such as `page`, `panel`, and `tab`.
4. Match against graph node names. Use the node's module and note to resolve ambiguity. Never select a weak match merely to avoid a gap.
5. For a new feature, start at the module landing node. For an existing feature, infer the start only from its unchanged `Background:` steps.
6. Run shortest-path breadth-first search over the graph.
7. Treat `has_mega_menu: true` as access to the `MegaMenu` transition. Render the open-and-select operation as one navigation step.
8. Preserve each graph edge's action label when translating it into the repository's existing Gherkin phrasing.

## Results

Record for each target:

```text
target | module | start | path | hopCount | frameworkGap | resolution
```

`resolution` is `resolved`, `unmapped`, or `unreachable`.

- For `resolved`, provide only the hops not already established by an existing `Background:`.
- For `unmapped` or `unreachable`, inspect page objects for context but do not invent a click path. Record a framework gap.
- If any graph node on the selected path has `framework_gap: true`, the scenario's framework readiness is `gap`.

Parse the graph once and reuse it for every ticket in the run.
