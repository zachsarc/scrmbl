# SCRMBL
## Developers: Zach Lariccia, Tyler Pyrzenski
## Welcome to SCRMBL, An Intelligent AI-powered Encryption/Decryption steganographic tool used to hide some top-secret data. Here is the sequence of SCRMBL using 3 different types of custom algorithms as listed below.
# Encryption and Steganography Workflow

## 🧩 Overview

**Scrmbl** is a Java-based encryption and decryption framework that reimagines a previous image-scrambling concept into a text-focused, multi-structure cipher system.  
The project demonstrates how **different data structures**—**maps**, **graphs**, and **trees**—can uniquely drive algorithmic encryption logic.

Each encryption “flavor” showcases a different structural philosophy:

### 🔸 Hard-Boiled (Map-Based Encryption)
- Uses Java’s `HashMap` to build a **deterministic substitution cipher**.  
- The cipher maps bytes based on a **key-derived permutation**, allowing quick lookups and reversibility through an inverse map.

### 🔸 Poached (Graph-Based Encryption)
- Models encryption as a **cycle graph** where each node (byte) connects to another based on **pseudo-random walks** derived from the key.  
- Reversible mapping is achieved by following the cycle’s edges backward.

### 🔸 Scrambled (Tree-Based Encryption)
- Builds a **binary search tree (BST)** from a key-seeded shuffled insertion order.  
- Encryption maps the tree’s **in-order traversal** to its **pre-order traversal**, providing structure-dependent variability.

## ⚙️ Encryption Process

1. The user provides a **key** and selects one of the three algorithms.  
2. The program generates a **deterministic permutation** of 256 byte values.  
3. The permutation defines the **encryption substitution**, while its inverse enables **decryption**.

**Core Concept:**  
Each algorithm leverages its data structure’s relationships—**map lookups**, **graph cycles**, or **tree traversals**—to transform plaintext into ciphertext in structurally distinct ways.

## 💡 Example Use Case

**Algorithm:** `Scrambled (Tree-Based)`  
**Plaintext:** `Hello, Scrmbl!`  
**Cipher (hex):** `9F64A34E...`  
**Recovered:** `Hello, Scrmbl!`

Different algorithms produce unique encrypted outputs for the same plaintext and key, but all are **fully reversible** with the correct key.

## Select your encryption preference

Hard-Boiled – Map-based

Poached – Graph-based

Scrambled – Tree-based
