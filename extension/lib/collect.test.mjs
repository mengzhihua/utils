import assert from 'node:assert/strict'
import { cpuPercents, formatBytes } from './collect.js'

assert.equal(formatBytes(0), '0 B')
assert.equal(formatBytes(1024), '1.0 KB')
assert.equal(formatBytes(1024 * 1024 * 1024), '1.0 GB')
assert.equal(formatBytes(-1), '—')
assert.equal(formatBytes(Number.NaN), '—')

const idleThenBusy = cpuPercents(
  { processors: [{ usage: { idle: 80, kernel: 10, user: 10, total: 100 } }] },
  { processors: [{ usage: { idle: 90, kernel: 50, user: 60, total: 200 } }] }
)
assert.deepEqual(idleThenBusy, [90])

const empty = cpuPercents(null, { processors: [{ usage: { idle: 1, total: 2 } }] })
assert.deepEqual(empty, [])

const noDelta = cpuPercents(
  { processors: [{ usage: { idle: 10, total: 20 } }] },
  { processors: [{ usage: { idle: 10, total: 20 } }] }
)
assert.deepEqual(noDelta, [0])

console.log('collect.test.mjs ok')
