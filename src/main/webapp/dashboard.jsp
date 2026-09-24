<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>RoxCode - Dashboard</title>

  <!-- Font Awesome -->
  <link rel="stylesheet"
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

  <!-- CSS -->
  <link rel="stylesheet" href="css/dashboard.css">
</head>

<body>

<!-- Background Shapes -->
<div class="bg-shape bg-shape-1"></div>
<div class="bg-shape bg-shape-2"></div>

<!-- Dashboard -->
<div class="dashboard-container">

  <!-- ================= SIDEBAR ================= -->
  <aside class="sidebar">

    <!-- Brand -->
    <div class="app-brand">

      <svg width="28"
           height="28"
           viewBox="0 0 24 24"
           fill="none"
           stroke="#38b69b"
           stroke-width="2.5"
           stroke-linecap="round"
           stroke-linejoin="round">

        <rect x="3" y="3" width="7" height="7"></rect>
        <rect x="14" y="3" width="7" height="7"></rect>
        <rect x="14" y="14" width="7" height="7"></rect>
        <rect x="3" y="14" width="7" height="7"></rect>

      </svg>

      <span>RoxCode</span>

    </div>


    <!-- Profile -->
    <div class="profile-section">

      <div class="avatar">P</div>

      <div class="profile-name">
        P Panigrahi
      </div>

      <div class="profile-email">
        Panigrahi@roxcode.io
      </div>

    </div>


    <!-- Navigation -->
    <nav class="nav-menu">

      <a href="#" class="nav-item active">
        <i class="fa-solid fa-chart-pie"></i>
        <span>Dashboard</span>
      </a>

      <a href="#" class="nav-item">
        <i class="fa-solid fa-code"></i>
        <span>Problems</span>
      </a>

      <a href="#" class="nav-item">
        <i class="fa-solid fa-list-check"></i>
        <span>Submissions</span>
      </a>

      <a href="#" class="nav-item">
        <i class="fa-solid fa-terminal"></i>
        <span>Playground</span>
      </a>

      <a href="#" class="nav-item">
        <i class="fa-solid fa-chart-line"></i>
        <span>Analytics</span>
      </a>

      <a href="#" class="nav-item">
        <i class="fa-solid fa-shield-halved"></i>
        <span>System Check</span>
      </a>

      <a href="#" class="nav-item">
        <i class="fa-solid fa-gear"></i>
        <span>Settings</span>
      </a>

    </nav>


    <!-- Logout -->
    <a href="login.jsp" class="logout-btn">

      <i class="fa-solid fa-right-from-bracket"></i>

      <span>Logout</span>

    </a>

  </aside>


  <!-- ================= MAIN CONTENT ================= -->
  <main class="main-canvas">


    <!-- Header -->
    <header class="header-bar">

      <h1 class="greeting">
        Welcome Pravat !
      </h1>


      <div class="header-controls">

        <!-- Search -->
        <div class="pill-search-box">

          <i class="fa-solid fa-magnifying-glass"></i>

          <input
                  type="text"
                  placeholder="Search challenges or tags..."
          >

        </div>


        <!-- Theme -->
        <button class="icon-bubble">
          <i class="fa-solid fa-sun"></i>
        </button>


        <!-- Notification -->
        <button class="icon-bubble">
          <i class="fa-solid fa-bell"></i>
        </button>

      </div>

    </header>



    <!-- ================= STAT CARDS ================= -->
    <section class="overview-bar">


      <!-- Assigned -->
      <div class="stat-card">

        <div class="stat-icon">
          <i class="fa-solid fa-box-archive"></i>
        </div>

        <div class="stat-details">

          <div class="val">
            5,483
          </div>

          <div class="lbl">
            Assigned Tests
          </div>

        </div>

      </div>


      <!-- Completed -->
      <div class="stat-card">

        <div class="stat-icon">
          <i class="fa-solid fa-layer-group"></i>
        </div>

        <div class="stat-details">

          <div class="val">
            2,859
          </div>

          <div class="lbl">
            Completed Tasks
          </div>

        </div>

      </div>


      <!-- Passed -->
      <div class="stat-card">

        <div class="stat-icon">
          <i class="fa-solid fa-arrow-trend-up"></i>
        </div>

        <div class="stat-details">

          <div class="val">
            5,483
          </div>

          <div class="lbl">
            Total Tests Passed
          </div>

        </div>

      </div>


      <!-- Pending -->
      <div class="stat-card alert">

        <div class="stat-icon">
          <i class="fa-solid fa-clock"></i>
        </div>

        <div class="stat-details">

          <div class="val">
            38
          </div>

          <div class="lbl">
            Pending Reviews
          </div>

        </div>

      </div>

    </section>



    <!-- ================= MIDDLE SECTION ================= -->
    <section class="grid-row-middle">


      <!-- Candidate Score -->
      <div class="dashboard-card">

        <div class="card-title">

                        <span>
                            Candidate Score
                        </span>

          <i class="fa-solid fa-ellipsis-vertical"></i>

        </div>


        <div class="metric-hero">

          <div class="icon-lg">
            <i class="fa-solid fa-users"></i>
          </div>

          <div class="big-val">
            583 K
          </div>

          <div class="sub-lbl">
            Total Points Earned
          </div>

        </div>

      </div>



      <!-- Topic Mastery -->
      <div class="dashboard-card">

        <div class="card-title">
          Topic Mastery
        </div>


        <div class="chart-container">

          <div class="donut-chart">

            <div class="donut-inner">
              68%
            </div>

          </div>


          <div class="legend">

            <div class="legend-item">

              <div class="legend-color primary"></div>

              <span>
                                    Algorithms
                                </span>

            </div>


            <div class="legend-item">

              <div class="legend-color secondary"></div>

              <span>
                                    Data Struct.
                                </span>

            </div>

          </div>

        </div>

      </div>



      <!-- Languages -->
      <div class="dashboard-card">

        <div class="card-title">
          Top Languages by Submissions
        </div>


        <div class="bar-list">


          <div class="bar-item">

                            <span class="bar-label">
                                Python 3
                            </span>

            <div class="bar-track">
              <div
                      class="bar-fill"
                      style="width:85%;">
              </div>
            </div>

            <span class="bar-val">
                                874k
                            </span>

          </div>



          <div class="bar-item">

                            <span class="bar-label">
                                C++ 20
                            </span>

            <div class="bar-track">
              <div
                      class="bar-fill"
                      style="width:70%;">
              </div>
            </div>

            <span class="bar-val">
                                721k
                            </span>

          </div>



          <div class="bar-item">

                            <span class="bar-label">
                                Java 17
                            </span>

            <div class="bar-track">
              <div
                      class="bar-fill"
                      style="width:55%;">
              </div>
            </div>

            <span class="bar-val">
                                590k
                            </span>

          </div>



          <div class="bar-item">

                            <span class="bar-label">
                                JavaScript
                            </span>

            <div class="bar-track">
              <div
                      class="bar-fill"
                      style="width:40%;">
              </div>
            </div>

            <span class="bar-val">
                                395k
                            </span>

          </div>

        </div>

      </div>

    </section>



    <!-- ================= BOTTOM CHART ================= -->
    <section class="dashboard-card grid-row-bottom">


      <div class="card-title">

                    <span>
                        Score History vs Execution Time
                    </span>

        <span class="last-six">
                        Last 6 months
                    </span>

      </div>


      <div class="line-chart-mock">

        <svg
                class="svg-chart"
                viewBox="0 0 500 100"
                preserveAspectRatio="none">

          <!-- Grid -->
          <line
                  x1="0"
                  y1="20"
                  x2="500"
                  y2="20"
                  stroke="#E1EFEA"
                  stroke-dasharray="4"
          />

          <line
                  x1="0"
                  y1="60"
                  x2="500"
                  y2="60"
                  stroke="#E1EFEA"
                  stroke-dasharray="4"
          />


          <!-- Area -->
          <path
                  d="M 0 70
                               Q 80 80, 160 60
                               T 320 30
                               T 500 40
                               L 500 100
                               L 0 100 Z"
                  fill="rgba(56,182,155,0.12)"
          />


          <!-- Line -->
          <path
                  d="M 0 70
                               Q 80 80, 160 60
                               T 320 30
                               T 500 40"
                  fill="none"
                  stroke="#38b69b"
                  stroke-width="3"
          />

        </svg>


        <!-- Months -->
        <div class="chart-labels">

          <span>Dec</span>
          <span>Jan</span>
          <span>Feb</span>
          <span>Mar</span>
          <span>Apr</span>
          <span>May</span>
          <span>Jun</span>

        </div>

      </div>

    </section>

  </main>

</div>

</body>
</html>