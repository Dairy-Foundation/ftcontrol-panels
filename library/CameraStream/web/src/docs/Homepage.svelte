<script lang="ts">
  import type { PluginConfig } from "ftc-panels"
  import { Title, Paragraph, AutoplayVideo, CodeBlock } from "ftc-panels/docs"

  let {
    info,
  }: {
    info: PluginConfig
  } = $props()

  import video from "./video.mp4"
  import code from "@examples/camerastream/TestCameraStream.kt?raw"
</script>

<Title level={1}>Camera Stream</Title>
<AutoplayVideo src={video} alt={"Stream Showcase"} />

<Paragraph>You can stream standard FTC Cameras.</Paragraph>

<Title level={1}>Example Usage</Title>

<CodeBlock language={"kotlin"} {code} />
