import assert from 'node:assert/strict'
import test from 'node:test'

import { formatFeature } from './format-feature.mjs'

test('does not put a blank line between the last step and Examples', () => {
  const formatted = formatFeature(`Feature: spacing
  Scenario Outline: no gap
    Given value <value>
    Examples:
      | value |
      | one |
`)

  assert.match(formatted, /Given value <value>\n    Examples:/)
  assert.doesNotMatch(formatted, /Given value <value>\n\n    Examples:/)
})

test('removes the blank line before tagged Examples', () => {
  const formatted = formatFeature(`Feature: tagged examples
  Scenario Outline: no gap
    Given value <value>
    @dataset
    Examples: data
      | value |
      | one |
`)

  assert.match(formatted, /Given value <value>\n    @dataset\n    Examples: data/)
})

test('keeps Source directly above @todo', () => {
  const formatted = formatFeature(`Feature: source tags
  # Source: QA-1
  @todo
  Scenario: traceable case
    Given one
`)

  assert.match(formatted, /# Source: QA-1\n  @todo/)
  assert.doesNotMatch(formatted, /# Source: QA-1\n\n  @todo/)
})

test('separates Source from the previous scenario', () => {
  const formatted = formatFeature(`Feature: source spacing
  Scenario: previous case
    Given one
  # Source: QA-1
  @todo
  Scenario: traceable case
    Given two
`)

  assert.match(formatted, /Given one\n\n  # Source: QA-1\n  @todo/)
})

test('retains the blank line between scenarios', () => {
  const formatted = formatFeature(`Feature: scenarios
  Scenario: first
    Given one
  Scenario: second
    Given two
`)

  assert.match(formatted, /Given one\n\n  Scenario: second/)
})
