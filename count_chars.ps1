# 统计代码字符数的PowerShell脚本

$projectRoot = "E:\Myproject\Game\JavaSTG"
$totalChars = 0
$fileCount = 0

# 定义要统计的文件类型
$fileTypes = @("*.java", "*.md", "*.json", "*.bat")

# 遍历所有文件类型
foreach ($ext in $fileTypes) {
    Write-Host "正在统计 $ext 文件..."
    $files = Get-ChildItem -Path $projectRoot -Recurse -Filter $ext -ErrorAction SilentlyContinue
    
    foreach ($file in $files) {
        try {
            # 读取文件内容并统计字符数
            $content = Get-Content -Path $file.FullName -Encoding UTF8 -Raw
            $charCount = $content.Length
            $totalChars += $charCount
            $fileCount++
            
            # 显示单个文件的统计结果
            Write-Host "$($file.FullName): $charCount 字符"
        }
        catch {
            Write-Host "无法读取文件: $($file.FullName)" -ForegroundColor Red
        }
    }
}

Write-Host "`n===== 统计结果 ====="
Write-Host "总文件数: $fileCount"
Write-Host "总字符数: $totalChars"
Write-Host "平均每个文件: $([math]::Round($totalChars / $fileCount, 2)) 字符"