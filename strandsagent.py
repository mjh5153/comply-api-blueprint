from strands import Agent, tool
from strands_tools import calculator, current_time

class Node:
    def __init__(self, key=0, val=0):
        self.key, self.val = key, val
        self.prev = self.next = None

class LRUCache:
    """
    LRU (Least Recently Used) Cache implementation with O(1) operations.

    Data Structure:
    - Hash map (self.cache): Enables O(1) key lookup and value retrieval
    - Doubly linked list: Maintains insertion order by recency (MRU at head, LRU at tail)
    - Sentinel nodes (self.head, self.tail): Eliminate boundary condition checks

    Time Complexity Analysis:
    - get(key):     O(1) - Hash map lookup is O(1), node removal/insertion on linked list is O(1)
    - put(key, val): O(1) - Hash map insertion/deletion is O(1), linked list operations are O(1)

    Space Complexity: O(capacity) - We store at most 'capacity' nodes in the hash map and linked list

    Why This Achieves O(1):
    - Hash map lookup: Direct access to node via key in O(1)
    - Linked list insert/remove at known positions: O(1) with pointer updates
    - Sentinel nodes: Prevent special cases for head/tail, all operations uniform O(1)

    Edge Cases Handled:
    - get() on missing key returns -1
    - put() on existing key updates value and moves node to front (MRU position)
    - Eviction at capacity removes the correct LRU node (tail.prev) from both list and hash map
    """

    def __init__(self, capacity: int):
        self.cap = capacity
        self.cache = {}          # key -> Node (hash map for O(1) lookup)
        self.head = Node()       # dummy head (MRU side - most recently used)
        self.tail = Node()       # dummy tail (LRU side - least recently used)
        self.head.next = self.tail
        self.tail.prev = self.head

    def _remove(self, node):
        """Remove node from doubly linked list in O(1) time."""
        node.prev.next = node.next
        node.next.prev = node.prev

    def _add_front(self, node):
        """Add node to front (after head) in O(1) time. Marks as most recently used."""
        node.next = self.head.next
        node.prev = self.head
        self.head.next.prev = node
        self.head.next = node

    def get(self, key: int) -> int:
        """
        Get value for key in O(1) time.
        Marks the accessed key as most recently used by moving to front.

        Edge case: Returns -1 if key not in cache.
        """
        if key not in self.cache:  # Edge case: missing key
            return -1
        node = self.cache[key]     # O(1) hash map lookup
        self._remove(node)         # O(1) remove from current position
        self._add_front(node)      # O(1) move to front (MRU)
        return node.val

    def put(self, key: int, value: int) -> None:
        """
        Put key-value pair in cache in O(1) time.

        Edge cases handled:
        - Existing key: Update value and move to front
        - New key at capacity: Evict LRU node (tail.prev) before adding
        """
        if key in self.cache:      # Edge case: existing key
            self._remove(self.cache[key])  # O(1) remove old node

        node = Node(key, value)
        self.cache[key] = node     # O(1) hash map insertion
        self._add_front(node)      # O(1) add to front (MRU)

        if len(self.cache) > self.cap:  # Edge case: capacity exceeded
            lru = self.tail.prev   # Get LRU node (before tail)
            self._remove(lru)      # O(1) remove from list
            del self.cache[lru.key]  # O(1) remove from hash map
# 1. Define a custom tool
# @tool
# def count_keyword(text: str, keyword: str) -> int:
#     """
#     Count how many times a keyword appears in a chunk of text.
#
#     Args:
#         text: The text to search.
#         keyword: Case-insensitive keyword to count.
#     Returns:
#         Number of occurrences.
#     """
#     if not isinstance(text, str) or not isinstance(keyword, str):
#         return 0
#
#     if not keyword:
#         return 0
#
#     return text.lower().count(keyword.lower())
#
#
# # 2. Create the agent using built-in and custom tools
# strandsagent = Agent(
#     tools=[
#         calculator,     # from strands-agents-tools
#         current_time,   # from strands-agents-tools
#         count_keyword,  # our custom exam-themed tool
#     ],
#
#     system_prompt=(
#         "You are a helpful generative AI assistant that is also a chicken. Cluck a lot. "
#         "Use tools when they help produce accurate answers."
#     ),
# )
#
#
# if __name__ == "__main__":
#     # 3. Ask the agent a question that uses the tools
#     message = """
# You have three tasks:
#
# 1. What time is it right now?
# 2. Calculate (3111696 / 74088).
# 3. In the text below, how many times does the word 'prompt' appear?
#
# Text:
# "In this course, we practice prompt engineering.
# Good prompts lead to better model behavior, and prompt patterns
# are a key part of generative AI development."
# """
#     result = strandsagent(message)
#
#     # result is an AgentResult; print the final answer
#     print("\n=== AGENT RESPONSE ===")
#     print(result)
