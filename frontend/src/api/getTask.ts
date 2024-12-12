import { TaskDTO } from "../services/interfaces/task.dto";

export default async function getTasks(supportType?: string): Promise<TaskDTO[]> {
    const initialUrl: string = "localhost:8080/task/SupportTypeOrStatusFilter?:";

    const finalUrl:string = supportType ? `${initialUrl}supportType=${supportType}` : initialUrl

    const response: Response = await fetch(finalUrl)

    if (!response.ok) {
        throw new Error("Tarefas não identificadas.")
    }


    return response.json()
}