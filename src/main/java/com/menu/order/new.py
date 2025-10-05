import sys
from collections import deque

def main():
    data = sys.stdin.read().split()
    if not data:
        return
    
    it = iter(data)
    N = int(next(it))
    K = int(next(it))
    
    if K > N:
        print("NO SOLUTION")
        return
    
    arr = [int(next(it)) for _ in range(N)]
    
    # Compute prefix sums
    prefix = [0] * (N + 1)
    for i in range(N):
        prefix[i + 1] = prefix[i] + arr[i]
    
    # DP with space optimization
    INF = float('inf')
    prev_dp = [INF] * (N + 1)
    curr_dp = [INF] * (N + 1)
    
    # Base case: k = 1
    for i in range(1, N + 1):
        prev_dp[i] = prefix[i] * prefix[i]
    
    if K == 1:
        print(prev_dp[N])
        return
    
    # Convex Hull Trick with deque
    def bad(l1, l2, l3):
        # Check if line2 is redundant
        m1, b1 = l1
        m2, b2 = l2
        m3, b3 = l3
        # Using cross product to avoid division
        # (b2 - b1) / (m1 - m2) >= (b3 - b2) / (m2 - m3)
        # (b2 - b1) * (m2 - m3) >= (b3 - b2) * (m1 - m2)
        return (b2 - b1) * (m2 - m3) >= (b3 - b2) * (m1 - m2)
    
    # DP for k = 2 to K
    for k in range(2, K + 1):
        curr_dp = [INF] * (N + 1)
        
        # CHT deque: stores (slope, intercept) tuples
        dq = deque()
        
        # Add line for j = k-1
        j = k - 1
        m = -2 * prefix[j]
        b = prev_dp[j] + prefix[j] * prefix[j]
        dq.append((m, b))
        
        for i in range(k, N + 1):
            x = prefix[i]
            
            # Remove lines from front that are no longer optimal
            while len(dq) >= 2:
                m1, b1 = dq[0]
                m2, b2 = dq[1]
                if m1 * x + b1 >= m2 * x + b2:
                    dq.popleft()
                else:
                    break
            
            # Query the best line
            if dq:
                m, b = dq[0]
                curr_dp[i] = m * x + b + x * x
            
            # Add new line for j = i (if not last iteration)
            if i < N:
                m_new = -2 * prefix[i]
                b_new = prev_dp[i] + prefix[i] * prefix[i]
                
                # Remove lines from back that become suboptimal
                while len(dq) >= 2 and bad(dq[-2], dq[-1], (m_new, b_new)):
                    dq.pop()
                
                dq.append((m_new, b_new))
        
        prev_dp, curr_dp = curr_dp, prev_dp
    
    print(prev_dp[N])

if __name__ == "__main__":
    main()